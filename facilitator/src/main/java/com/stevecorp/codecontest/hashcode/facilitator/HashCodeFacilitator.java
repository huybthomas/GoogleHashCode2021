package com.stevecorp.codecontest.hashcode.facilitator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.Algorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmParameter;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.BasicAlgorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.ParameterizedAlgorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter.BoundedParameter;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter.EnumeratedParameter;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputParser;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputProducer;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputValidator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.score.ScoreCalculator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier.ALL_INPUT_FILES;
import static com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier.SELECTED_INPUT_FILES;
import static com.stevecorp.codecontest.hashcode.facilitator.util.ClassUtils.simpleName;
import static com.stevecorp.codecontest.hashcode.facilitator.util.CollectionUtils.join;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.cleanFolderContents;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.getFilePathsFromFolder;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.getFolder;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.getFolderFromResources;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.readFileContents;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.toFileName;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.writeToFile;
import static java.text.MessageFormat.format;

@SuppressWarnings({ "unused, unchecked", "rawtypes", "FieldCanBeLocal", "OptionalUsedAsFieldOrParameterType" })
public class HashCodeFacilitator<T extends InputModel, U extends OutputModel> {

    private static final Path DEFAULT_INPUT_FOLDER = getFolderFromResources("input");
    private static final Path DEFAULT_OUTPUT_FOLDER = getFolderFromResources("output");
    private static final int PROGRESS_BAR_LENGTH = 50;

    private final InputSpecifier inputSpecifier;
    private final List<String> inputFileNames;
    private final InputParser<T> inputParser;
    private final List<AlgorithmSpecification<T, U>> algorithmSpecifications;
    private final Optional<OutputValidator<T, U>> outputValidator;
    private final ScoreCalculator<T, U> scoreCalculator;
    private final OutputProducer<U> outputProducer;
    private final Path inputFolder;
    private final Path outputFolder;

    private HashCodeFacilitator(final ConfigBuilder<T, U> builder) {
        this.inputSpecifier = builder.inputSpecifier;
        this.inputFileNames = builder.inputFileNames;
        this.inputParser = builder.inputParser;
        this.algorithmSpecifications = builder.algorithmSpecifications;
        this.outputValidator = Optional.ofNullable(builder.outputValidator);
        this.scoreCalculator = builder.scoreCalculator;
        this.outputProducer = builder.outputProducer;
        this.inputFolder = builder.inputFolder != null ? builder.inputFolder : DEFAULT_INPUT_FOLDER;
        this.outputFolder = builder.outputFolder != null ? builder.outputFolder : DEFAULT_OUTPUT_FOLDER;
    }

    public static Configurator_InputSpecifier<?, ?> configurator() {
        return new ConfigBuilder<>().configurator();
    }

    public void run() {
        preFacilitatorSetup();
        for (final Path inputFilePath : getInputFilePaths()) {
            System.out.println(format("Processing input file: ''{0}''", toFileName(inputFilePath)));
            final T input = inputParser.parseInput(readFileContents(inputFilePath));
            final ScoreTracker<T, U> scoreTracker = new ScoreTracker<>();
            final long numberOfScenarios = getNumberOfScenarios();
            long progress = 0;
            for (final AlgorithmSpecification<T, U> algorithmSpecification : algorithmSpecifications) {
                final Algorithm<T, U> algorithm = algorithmSpecification.getAlgorithm();
                if (algorithm instanceof BasicAlgorithm) {
                    final BasicAlgorithm<T, U> basicAlgorithm = (BasicAlgorithm<T, U>) algorithm;
                    final T clonedInput = input.cloneInput();
                    final U output = basicAlgorithm.solve(clonedInput);
                    outputValidator.ifPresent(validator -> validator.validateOutput(clonedInput, output));
                    final long score = scoreCalculator.calculateScore(clonedInput, output);
                    scoreTracker.handleUpdate(score, output, basicAlgorithm.getClass());
                    printProgressBar(++progress, numberOfScenarios);
                } else if (algorithm instanceof ParameterizedAlgorithm) {
                    final ParameterizedAlgorithm<T, U> parameterizedAlgorithm = (ParameterizedAlgorithm<T, U>) algorithm;
                    final List<ParameterState<?>> parameterStates = algorithmSpecification.getParameters().stream()
                            .map(this::toParameterState)
                            .collect(Collectors.toList());
                    final Map<String, Object> parameterPermutation = initializeParameterMap(parameterStates);
                    for (final ParameterState<?> parameterState : parameterStates) {
                        while (parameterState.hasNext()) {
                            parameterizedAlgorithm.handleParameters(parameterPermutation);
                            final T clonedInput = input.cloneInput();
                            final U output = parameterizedAlgorithm.solve(clonedInput);
                            outputValidator.ifPresent(validator -> validator.validateOutput(clonedInput, output));
                            final long score = scoreCalculator.calculateScore(clonedInput, output);
                            scoreTracker.handleUpdate(score, output, parameterizedAlgorithm.getClass(), parameterPermutation);
                            printProgressBar(++progress, numberOfScenarios);
                            parameterPermutation.put(parameterState.parameter.getName(), parameterState.next());
                        }
                    }
                }
            }
            scoreTracker.printReport();
            writeToFile(outputFolder, inputFilePath, outputProducer.produceOutput(scoreTracker.bestOutput));
            System.out.print("\n");
        }
    }

    private void preFacilitatorSetup() {
        if (!outputFolder.equals(DEFAULT_OUTPUT_FOLDER)) {
            cleanFolderContents(outputFolder);
        }
    }

    private List<Path> getInputFilePaths() {
        return getFilePathsFromFolder(inputFolder, fileName ->
                inputSpecifier == ALL_INPUT_FILES || inputFileNames.stream().anyMatch(fileName::contains));
    }

    private long getNumberOfScenarios() {
        long numberOfScenarios = 0;
        numberOfScenarios += algorithmSpecifications.stream()
                .filter(algorithmSpecification -> algorithmSpecification.getAlgorithm() instanceof BasicAlgorithm)
                .count();
        numberOfScenarios += algorithmSpecifications.stream()
                .filter(algorithmSpecification -> algorithmSpecification.getAlgorithm() instanceof ParameterizedAlgorithm)
                .mapToLong(algorithmSpecification -> algorithmSpecification.getParameters().stream()
                        .mapToLong(AlgorithmParameter::getNumberOfScenarios)
                        .reduce(1, Math::multiplyExact))
                .sum();
        return numberOfScenarios;
    }

    private ParameterState<?> toParameterState(final AlgorithmParameter parameter) {
        if (parameter instanceof BoundedParameter) {
            return new ParameterState_Bounded((BoundedParameter) parameter);
        }
        if (parameter instanceof EnumeratedParameter) {
            return new ParameterState_Enumerated((EnumeratedParameter) parameter);
        }
        throw new RuntimeException(format("Unsupported algorithm parameter type: ''{}''", simpleName(parameter)));
    }

    private Map<String, Object> initializeParameterMap(final List<ParameterState<?>> parameterStates) {
        return parameterStates.stream()
                .collect(Collectors.toMap(parameterState -> parameterState.parameter.getName(), ParameterState::next));
    }

    private void doAlgorithmIteration(final T input, final Algorithm<T, U> algorithm) {
    }

    private void printProgressBar(final long currentScenarioIndex, final long totalNumberOfScenarios) {
        final double percentualProgress = 1.0 * currentScenarioIndex / totalNumberOfScenarios;
        final long progressBarProgress = (long) Math.floor(PROGRESS_BAR_LENGTH * percentualProgress);
        final StringBuilder progressBar = new StringBuilder();
        progressBar.append('[');
        IntStream.range(0, (int) progressBarProgress).forEach(index -> progressBar.append("="));
        IntStream.range(0, (int) (PROGRESS_BAR_LENGTH - progressBarProgress)).forEach(index -> progressBar.append(" "));
        progressBar.append("]");
        System.out.print(format("\r{0} {1}%", progressBar.toString(), (percentualProgress * 100)));
        if (currentScenarioIndex == totalNumberOfScenarios) {
            System.out.print("\n");
        }
    }

    /**************************************************************************************************************
     ***   Class to track the best algorithm per input (with additional details)                                ***
     **************************************************************************************************************/

    public static class ScoreTracker<T extends InputModel, U extends OutputModel> {

        long bestScore;
        U bestOutput;
        Class<? extends Algorithm> bestAlgorithm;
        Optional<Map<String, Object>> bestAlgorithmParameters;

        public ScoreTracker() {
            this.bestScore = Long.MIN_VALUE;
        }

        void handleUpdate(final long score, final U output, final Class<? extends BasicAlgorithm> algorithmClass) {
            handleUpdate(score, output, algorithmClass, null);
        }

        void handleUpdate(final long score, final U output, final Class<? extends Algorithm> algorithmClass, final Map<String, Object> parameters) {
            if (score > bestScore) {
                bestScore = score;
                bestOutput = output;
                bestAlgorithm = algorithmClass;
                bestAlgorithmParameters = Optional.ofNullable(parameters);
            }
        }

        void printReport() {
            System.out.println("Optimal solution for input:");
            System.out.println(format("\tAlgorithm: {0}", simpleName(bestAlgorithm)));
            System.out.println(format("\tScore: {0}", bestScore));
            bestAlgorithmParameters.ifPresent(stringObjectMap -> System.out.println(format("\tParameters: {0}", stringObjectMap)));
            System.out.print("\n");
        }

    }

    /**************************************************************************************************************
     ***   Classes for parameter permutation streaming                                                          ***
     **************************************************************************************************************/

    public static abstract class ParameterState<V extends AlgorithmParameter>  {

        V parameter;

        abstract boolean hasNext();
        abstract Object next();

        public ParameterState(final V parameter) {
            this.parameter = parameter;
        }

    }

    public static final class ParameterState_Bounded extends ParameterState<BoundedParameter>  {

        Number value;

        public ParameterState_Bounded(final BoundedParameter parameter) {
            super(parameter);
            this.value = parameter.getLowerLimit();
        }

        @Override
        boolean hasNext() {
            return value.doubleValue() <= parameter.getUpperLimit();
        }

        @Override
        Number next() {
            final Number toReturn = value;
            value = value.doubleValue() + parameter.getStepSize();
            return toReturn;
        }

    }

    public static final class ParameterState_Enumerated extends ParameterState<EnumeratedParameter> {

        int index;

        public ParameterState_Enumerated(final EnumeratedParameter parameter) {
            super(parameter);
            this.index = 0;
        }

        @Override
        boolean hasNext() {
            return index < parameter.getNumberOfScenarios();
        }

        @Override
        Object next() {
            return parameter.getValues().get(index++);
        }

    }

    /**************************************************************************************************************
     ***   Definition of Builder (chaining) classes to enforce a method order                                   ***
     **************************************************************************************************************/

    public static final class ConfigBuilder<T extends InputModel, U extends OutputModel> {

        InputSpecifier inputSpecifier;
        List<String> inputFileNames;
        InputParser<T> inputParser;
        List<AlgorithmSpecification<T, U>> algorithmSpecifications;
        OutputValidator<T, U> outputValidator;
        ScoreCalculator<T, U> scoreCalculator;
        OutputProducer<U> outputProducer;
        Path inputFolder;
        Path outputFolder;

        private ConfigBuilder() {}

        public Configurator_InputSpecifier<T, U> configurator() {
            return new Configurator_InputSpecifier<>(this);
        }

    }

    public static final class Configurator_InputSpecifier<T extends  InputModel, U extends OutputModel> {

        final ConfigBuilder<T, U> configBuilder;

        Configurator_InputSpecifier(final ConfigBuilder<T, U> configBuilder) {
            this.configBuilder = configBuilder;
        }

        public Configurator_InputParser<T, U> forAllInputFiles() {
            configBuilder.inputSpecifier = ALL_INPUT_FILES;
            configBuilder.inputFileNames = new ArrayList<>();
            return new Configurator_InputParser<>(this);
        }

        public Configurator_InputParser<T, U> forASingleInputFile(final String inputFileName) {
            configBuilder.inputSpecifier = SELECTED_INPUT_FILES;
            configBuilder.inputFileNames = List.of(inputFileName);
            return new Configurator_InputParser<>(this);
        }

        public Configurator_InputParser<T, U> forSelectedInputFiles(final String inputFileName, final String... additionalInputFileNames) {
            configBuilder.inputSpecifier = SELECTED_INPUT_FILES;
            configBuilder.inputFileNames = join(inputFileName, additionalInputFileNames);
            return new Configurator_InputParser<>(this);
        }

    }

    public static final class Configurator_InputParser<T extends InputModel, U extends OutputModel> {

        final ConfigBuilder<T, U> configBuilder;

        Configurator_InputParser(final Configurator_InputSpecifier<T, U> configurator) {
            this.configBuilder = configurator.configBuilder;
        }

        public Configurator_Algorithm<T, U> withInputParser(final InputParser inputParser) {
            configBuilder.inputParser = inputParser;
            return new Configurator_Algorithm<>(this);
        }

    }

    public static final class Configurator_Algorithm<T extends InputModel, U extends OutputModel> {

        final ConfigBuilder<T, U> configBuilder;

        Configurator_Algorithm(final Configurator_InputParser<T, U> configurator) {
            this.configBuilder = configurator.configBuilder;
        }

        public Configurator_OutputValidator<T, U> withAlgorithms(final AlgorithmSpecification algorithmSpecifications, final AlgorithmSpecification... additionalAlgorithmSpecifications) {
            configBuilder.algorithmSpecifications = join(algorithmSpecifications, additionalAlgorithmSpecifications);
            return new Configurator_OutputValidator<>(this);
        }

    }

    public static final class Configurator_OutputValidator<T extends InputModel, U extends OutputModel> {

        final ConfigBuilder<T, U> configBuilder;

        Configurator_OutputValidator(final Configurator_Algorithm<T, U> configurator) {
            this.configBuilder = configurator.configBuilder;
        }

        public Configurator_ScoreCalculator<T, U> withoutOutputValidation() {
            return new Configurator_ScoreCalculator<>(this);
        }

        public Configurator_ScoreCalculator<T, U> withOutputValidator(final OutputValidator outputValidator) {
            configBuilder.outputValidator = outputValidator;
            return new Configurator_ScoreCalculator<>(this);
        }

    }

    public static final class Configurator_ScoreCalculator<T extends InputModel, U extends OutputModel> {

        final ConfigBuilder<T, U> configBuilder;

        Configurator_ScoreCalculator(final Configurator_OutputValidator<T, U> configurator) {
            this.configBuilder = configurator.configBuilder;
        }

        public Configurator_OutputProducer<T, U> withScoreCalculator(final ScoreCalculator scoreCalculator) {
            configBuilder.scoreCalculator = scoreCalculator;
            return new Configurator_OutputProducer<>(this);
        }

    }

    public static final class Configurator_OutputProducer<T extends InputModel, U extends OutputModel> {

        final ConfigBuilder<T, U> configBuilder;

        Configurator_OutputProducer(final Configurator_ScoreCalculator<T, U> configurator) {
            this.configBuilder = configurator.configBuilder;
        }

        public Configurator_Final<T, U> withOutputProducer(final OutputProducer outputProducer) {
            configBuilder.outputProducer = outputProducer;
            return new Configurator_Final<>(this);
        }

    }

    public static final class Configurator_Final<T extends InputModel, U extends OutputModel> {

        final ConfigBuilder<T, U> configBuilder;

        Configurator_Final(final Configurator_OutputProducer<T, U> configBuilder) {
            this.configBuilder = configBuilder.configBuilder;
        }

        public Configurator_Final<T, U> withCustomInputFolder(final String fullInputFolderPath) {
            configBuilder.inputFolder = getFolder(fullInputFolderPath);
            return this;
        }

        public Configurator_Final<T, U> withCustomOutputFolder(final String fullOutputFolderPath) {
            configBuilder.outputFolder = getFolder(fullOutputFolderPath);
            return this;
        }

        public void run() {
            new HashCodeFacilitator<>(configBuilder).run();
        }

    }

}
