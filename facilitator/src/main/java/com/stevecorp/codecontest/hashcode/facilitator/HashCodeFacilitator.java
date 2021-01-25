package com.stevecorp.codecontest.hashcode.facilitator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.AlgorithmSpecificationConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.FinalConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.InputParserConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.InputSpecificationConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.OutputProducerConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.OutputValidatorConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.ScoreCalculatorConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputParser;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputProducer;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputValidator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.score.ScoreCalculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier.ALL_INPUT_FILES;
import static com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier.SINGLE_INPUT_FILE;
import static com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier.SPECIFIC_INPUT_FILES;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.getFolder;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.getFolderFromResources;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.readFileContents;

public class HashCodeFacilitator implements
        InputSpecificationConfigBuilder,
        InputParserConfigBuilder,
        AlgorithmSpecificationConfigBuilder,
        OutputValidatorConfigBuilder,
        ScoreCalculatorConfigBuilder,
        OutputProducerConfigBuilder,
        FinalConfigBuilder {

    /**
     * Setup step 1: Input file specification
     */
    private Path inputFileLocation;
    private Path outputFileLocation;
    private InputSpecifier inputSpecifier;
    private Set<String> selectedInputFileNames;

    /**
     * Setup step 2: Input parsing
     */
    private InputParser<? extends InputModel> inputParser;

    /**
     * Setup step 3: Algorithm selection
     */
    private List<AlgorithmSpecification<? extends InputModel, ? extends OutputModel>> algorithms;

    /**
     * Setup step 4: Output validation
     */
    private Optional<OutputValidator<? extends InputModel, ? extends OutputModel>> outputValidator;

    /**
     * Setup step 5: Algorithm output score calculator
     */
    private Object scoreCalculator;

    /**
     * Setup step 6: Algorithm output producer
     */
    private Object outputProducer;

    private HashCodeFacilitator() {
        this.inputFileLocation = getFolderFromResources("input");
        this.outputFileLocation = getFolderFromResources("output");
        this.outputValidator = Optional.empty();
    }

    public static InputSpecificationConfigBuilder configurator() {
        return new HashCodeFacilitator();
    }


    @Override
    public InputParserConfigBuilder forAllInputFiles() {
        this.inputSpecifier = ALL_INPUT_FILES;
        return this;
    }

    @Override
    public InputParserConfigBuilder forASingleInputFile(final String inputFileName) {
        this.inputSpecifier = SINGLE_INPUT_FILE;
        this.selectedInputFileNames = Set.of(inputFileName);
        return this;
    }

    @Override
    public InputParserConfigBuilder forSpecificInputFiles(final String... inputFileNames) {
        this.inputSpecifier = SPECIFIC_INPUT_FILES;
        this.selectedInputFileNames = new HashSet<>(Arrays.asList(inputFileNames));
        return this;
    }

    @Override
    public AlgorithmSpecificationConfigBuilder withInputParser(final InputParser<? extends InputModel> inputParser) {
        this.inputParser = inputParser;
        return this;
    }

    @Override
    public OutputValidatorConfigBuilder withAlgorithms(final AlgorithmSpecification<?, ?>... algorithms) {
        this.algorithms = Arrays.asList(algorithms);
        return this;
    }

    @Override
    public ScoreCalculatorConfigBuilder dontValidateOutput() {
        return this;
    }

    @Override
    public ScoreCalculatorConfigBuilder withOutputValidator(final OutputValidator<? extends InputModel, ? extends OutputModel> outputValidator) {
        this.outputValidator = Optional.of(outputValidator);
        return this;
    }

    @Override
    public OutputProducerConfigBuilder withScoreCalculator(final ScoreCalculator<? extends InputModel, ? extends OutputModel> scoreCalculator) {
        this.scoreCalculator = scoreCalculator;
        return this;
    }

    @Override
    public FinalConfigBuilder withOutputProducer(final OutputProducer<? extends OutputModel> outputProducer) {
        this.outputProducer = outputProducer;
        return this;
    }

    @Override
    public FinalConfigBuilder withCustomInputFolder(final String fullInputFolderPath) {
        this.inputFileLocation = getFolder(fullInputFolderPath);
        return this;
    }

    @Override
    public FinalConfigBuilder withCustomOutputFolder(final String fullOutputFolderPath) {
        this.outputFileLocation = getFolder(fullOutputFolderPath);
        return this;
    }

    @Override
    public void run() {
        final List<Path> inputFilePaths = getInputFilePaths();

        // Replace / Update this when actual implementation
        final InputModel input = inputParser.parseInput(readFileContents(inputFilePaths.get(0)));
    }

    private List<Path> getInputFilePaths() {
        try {
            return Files.list(inputFileLocation)
                    .filter(file -> !Files.isDirectory(file))
                    .filter(this::matchesSelectedFiles)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            throw new RuntimeException("Failed to get the selected input files!", e);
        }
    }

    private boolean matchesSelectedFiles(final Path inputFile) {
        final String inputFileName = inputFile.getFileName().toString();
        return inputSpecifier == ALL_INPUT_FILES || selectedInputFileNames.stream().anyMatch(inputFileName::contains);
    }

}
