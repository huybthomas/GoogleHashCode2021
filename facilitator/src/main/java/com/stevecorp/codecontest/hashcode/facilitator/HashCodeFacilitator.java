package com.stevecorp.codecontest.hashcode.facilitator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.Algorithm;
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
import java.util.Optional;

import static com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier.ALL_INPUT_FILES;
import static com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier.SELECTED_INPUT_FILES;
import static com.stevecorp.codecontest.hashcode.facilitator.util.CollectionUtils.join;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.getFilePathsFromFolder;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.getFolder;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.getFolderFromResources;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.readFileContents;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtils.toFileName;
import static java.text.MessageFormat.format;

@SuppressWarnings({ "unused, unchecked", "rawtypes", "FieldCanBeLocal", "OptionalUsedAsFieldOrParameterType" })
public class HashCodeFacilitator<T extends InputModel, U extends OutputModel> {

    private static final Path DEFAULT_INPUT_FOLDER = getFolderFromResources("input");
    private static final Path DEFAULT_OUTPUT_FOLDER = getFolderFromResources("output");

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
        // TODO CONTINUE, BEAUTIFY
        for (final Path inputFilePath : getInputFilePaths()) {
            System.out.println(format("Processing: ''{0}''", toFileName(inputFilePath)));
            final T input = inputParser.parseInput(readFileContents(inputFilePath));
            long progress = 0;
            final long numberOfScenarios = getNumberOfScenarios();
            for (final AlgorithmSpecification<T, U> algorithmSpecification : algorithmSpecifications) {
                final Algorithm<T, U> algorithm = algorithmSpecification.getAlgorithm();
                if (algorithm instanceof BasicAlgorithm) {
                    // handle basic algo
                } else if (algorithm instanceof ParameterizedAlgorithm) {
                    // handle parameterized algorithm, get all permutations
                }
            }
        }
    }

    private List<Path> getInputFilePaths() {
        return getFilePathsFromFolder(inputFolder, fileName ->
                inputSpecifier == ALL_INPUT_FILES || inputFileNames.stream().anyMatch(fileName::contains));
    }

    private long getNumberOfScenarios() {
        // TODO BEAUTIFY
        int count = 0;
        for (final AlgorithmSpecification<T, U> algorithmSpecification : algorithmSpecifications) {
            if (algorithmSpecification.getAlgorithm() instanceof BasicAlgorithm) {
                count += 1;
            } else {
                count += algorithmSpecification.getParameters().stream()
                        .mapToLong(parameter -> {
                            if (parameter instanceof BoundedParameter) {
                                final BoundedParameter boundedParameter = (BoundedParameter) parameter;
                                return (boundedParameter.getUpperLimit() - boundedParameter.getLowerLimit()) / boundedParameter.getStepSize();
                            }
                            if (parameter instanceof EnumeratedParameter) {
                                return ((EnumeratedParameter) parameter).getValues().size();
                            }
                            throw new RuntimeException("Bla");
                        })
                        .reduce(1, Math::multiplyExact);
            }
        }
        return count;
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
