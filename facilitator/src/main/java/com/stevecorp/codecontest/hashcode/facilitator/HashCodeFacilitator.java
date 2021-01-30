package com.stevecorp.codecontest.hashcode.facilitator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
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
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.getFilePathsFromFolder;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.getFolder;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.getFolderFromResources;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.readFileContents;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.writeToFile;
import static java.text.MessageFormat.format;

@SuppressWarnings({ "unused, unchecked", "rawtypes", "OptionalUsedAsFieldOrParameterType" })
public class HashCodeFacilitator<T extends InputModel, U extends OutputModel> {

    private static final Path DEFAULT_INPUT_FOLDER = getFolderFromResources("input");
    private static final Path DEFAULT_OUTPUT_FOLDER = getFolderFromResources("output");

    private final InputSpecifier inputSpecifier;
    private final List<String> inputFileNames;
    private final InputParser<T> inputParser;
    private final List<AlgorithmSpecification<T, U>> algorithms;
    private final Optional<OutputValidator<T, U>> outputValidator;
    private final ScoreCalculator<T, U> scoreCalculator;
    private final OutputProducer<U> outputProducer;
    private final Path inputFolder;
    private final Path outputFolder;

    private HashCodeFacilitator(final ConfigBuilder<T, U> builder) {
        this.inputSpecifier = builder.inputSpecifier;
        this.inputFileNames = builder.inputFileNames;
        this.inputParser = builder.inputParser;
        this.algorithms = builder.algorithms;
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
        final List<Path> inputFilePaths = getFilePathsFromFolder(inputFolder, fileName ->
                inputSpecifier == ALL_INPUT_FILES || inputFileNames.stream().anyMatch(fileName::contains));
        for (final Path inputFilePath : inputFilePaths) {
            System.out.println(format("Input file: {0}", inputFilePath.getFileName().toString()));
            final T input = inputParser.parseInput(readFileContents(inputFilePath));

            U bestOutput = null;
            long bestScore = Long.MIN_VALUE;
            AlgorithmSpecification<T, U> bestAlgorithm = null;

            for (final AlgorithmSpecification<T, U> algorithm : algorithms) {
                final T clonedInput = input.cloneInput();
                final U output = algorithm.solve(clonedInput);
                outputValidator.ifPresent(validator -> validator.validateOutput(clonedInput, output));
                final long score = scoreCalculator.calculateScore(clonedInput, output);
                System.out.println(format("Score for algorithm ''{0}'': {1}", algorithm.getAlgorithmName(), score));
                if (score > bestScore) {
                    bestScore = score;
                    bestOutput = output;
                    bestAlgorithm = algorithm;
                }
            }

            assert bestAlgorithm != null;

            System.out.println(format("Best algorithm: ''{0}'' - score: ''{1}''", bestAlgorithm.getClass().getSimpleName(), bestScore));

            final List<String> outputString = outputProducer.produceOutput(bestOutput);
            writeToFile(outputFolder, inputFilePath, outputString);
        }
    }

    /**************************************************************************************************************
     ***   Definition of Builder (chaining) classes to enforce a method order                                   ***
     **************************************************************************************************************/

    public static final class ConfigBuilder<T extends InputModel, U extends OutputModel> {

        InputSpecifier inputSpecifier;
        List<String> inputFileNames;
        InputParser<T> inputParser;
        List<AlgorithmSpecification<T, U>> algorithms;
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

        public Configurator_OutputValidator<T, U> withAlgorithms(final AlgorithmSpecification algorithm, final AlgorithmSpecification... additionalAlgorithms) {
            configBuilder.algorithms = join(algorithm, additionalAlgorithms);
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
