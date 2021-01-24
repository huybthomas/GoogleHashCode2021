package com.stevecorp.codecontest.hashcode.facilitator;

import com.stevecorp.codecontest.hashcode.facilitator.config.AlgorithmOutputValidatorConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.config.AlgorithmSpecificationConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.config.FinalConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.config.InputParserConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.config.InputSpecificationConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.config.OutputProducerConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.config.ScoreCalculatorConfigBuilder;
import com.stevecorp.codecontest.hashcode.facilitator.config.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.config.input.InputSpecifier;

import java.util.List;
import java.util.Optional;

//implements InputSpecificationConfigBuilder
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
    private InputSpecifier inputSpecifier;
    private List<String> inputFiles;

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
        this.algorithmOutputValidator = Optional.empty();
    }

    public static InputSpecificationConfigBuilder configurator() {
        return new HashCodeFacilitator();
    }


    @Override
    public InputParserConfigBuilder forAllInputFiles() {
        return null;
    }

    @Override
    public InputParserConfigBuilder forASingleInputFile(final String inputFile) {
        return null;
    }

    @Override
    public InputParserConfigBuilder forSpecificInputFiles(final String... inputFiles) {
        return null;
    }

    @Override
    public AlgorithmSpecificationConfigBuilder withInputParser(final Object o) {
        return null;
    }

    @Override
    public AlgorithmOutputValidatorConfigBuilder withAlgorithms(final AlgorithmSpecification... algorithms) {
        return null;
    }

    @Override
    public ScoreCalculatorConfigBuilder dontValidateAlgorithmOutput() {
        return null;
    }

    @Override
    public ScoreCalculatorConfigBuilder withAlgorithmOutputValidator(final Object algorithmOutputValidator) {
        return null;
    }

    @Override
    public OutputProducerConfigBuilder withScoreCalculator(final Object scoreCalculator) {
        return null;
    }

    @Override
    public FinalConfigBuilder withOutputProducer(final Object outputProducer) {
        return null;
    }

    @Override
    public FinalConfigBuilder withCustomInputFolder(final String fullInputFolderPath, final boolean createFolderIfAbsent) {
        return null;
    }

    @Override
    public FinalConfigBuilder withCustomOutputFolder(final String fullOutputFolderPath, final boolean createFolderIfAbsent) {
        return null;
    }

    @Override
    public void run() {

    }
}
