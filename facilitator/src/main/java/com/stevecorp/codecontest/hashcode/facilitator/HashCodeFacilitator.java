package com.stevecorp.codecontest.hashcode.facilitator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.AlgorithmOutputValidatorConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.AlgorithmSpecificationConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.FinalConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.InputParserConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.InputSpecificationConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.OutputProducerConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.ScoreCalculatorConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputSpecifier;

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

public class HashCodeFacilitator implements
        InputSpecificationConfigBuilder,
        InputParserConfigBuilder,
        AlgorithmSpecificationConfigBuilder,
        AlgorithmOutputValidatorConfigBuilder,
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
    private Object inputParser;

    /**
     * Setup step 3: Algorithm selection
     */
    private List<AlgorithmSpecification> algorithms;

    /**
     * Setup step 4: Algorithm output validation
     */
    private Optional<Object> algorithmOutputValidator;

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
        this.algorithmOutputValidator = Optional.empty();
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
    public AlgorithmSpecificationConfigBuilder withInputParser(final Object o) {
        return this;
    }

    @Override
    public AlgorithmOutputValidatorConfigBuilder withAlgorithms(final AlgorithmSpecification... algorithms) {
        return this;
    }

    @Override
    public ScoreCalculatorConfigBuilder dontValidateAlgorithmOutput() {
        return this;
    }

    @Override
    public ScoreCalculatorConfigBuilder withAlgorithmOutputValidator(final Object algorithmOutputValidator) {
        return this;
    }

    @Override
    public OutputProducerConfigBuilder withScoreCalculator(final Object scoreCalculator) {
        return this;
    }

    @Override
    public FinalConfigBuilder withOutputProducer(final Object outputProducer) {
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
