package com.stevecorp.codecontest.hashcode.facilitator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputParser;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputProducer;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputValidator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.score.ScoreCalculator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.getFilePathsFromFolder;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.getFolder;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.getFolderFromResources;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.readFileContents;
import static com.stevecorp.codecontest.hashcode.facilitator.util.FileUtil.writeToFile;

public class HashCodeFacilitator<T extends InputModel, U extends OutputModel> {

    private Path inputFileLocation = getFolderFromResources("input");
    private Path outputFileLocation = getFolderFromResources("output");

    private final InputParser<T> inputParser;
    private final ScoreCalculator<T, U> scoreCalculator;
    private final OutputProducer<U> outputProducer;

    private Set<String> selectedInputFileNames;
    private List<AlgorithmSpecification<T, U>> algorithms = new ArrayList<>();
    private OutputValidator<T, U> outputValidator;

    public HashCodeFacilitator(
            final InputParser<T> inputParser,
            final ScoreCalculator<T, U> scoreCalculator,
            final OutputProducer<U> outputProducer
    ) {
        this.inputParser = inputParser;
        this.scoreCalculator = scoreCalculator;
        this.outputProducer = outputProducer;
    }

    public HashCodeFacilitator<T, U> forSpecificInputFiles(final String... inputFileNames) {
        this.selectedInputFileNames = Set.of(inputFileNames);
        return this;
    }

    public HashCodeFacilitator<T, U> withAlgorithm(final AlgorithmSpecification<T, U> algorithm) {
        this.algorithms.add(algorithm);
        return this;
    }

    public HashCodeFacilitator<T, U> withOutputValidator(final OutputValidator<T, U> outputValidator) {
        this.outputValidator = outputValidator;
        return this;
    }

    public HashCodeFacilitator<T, U> withCustomInputFolder(final String fullInputFolderPath) {
        this.inputFileLocation = getFolder(fullInputFolderPath);
        return this;
    }

    public HashCodeFacilitator<T, U> withCustomOutputFolder(final String fullOutputFolderPath) {
        this.outputFileLocation = getFolder(fullOutputFolderPath);
        return this;
    }

    public void run() {
        if (algorithms.isEmpty()) {
            throw new RuntimeException("No algorithms found!");
        }

        final List<Path> inputFilePaths = getFilePathsFromFolder(inputFileLocation, fileName ->
                selectedInputFileNames == null || selectedInputFileNames.stream().anyMatch(fileName::contains));
        for (final Path inputFilePath : inputFilePaths) {
            final T input = inputParser.parseInput(readFileContents(inputFilePath));

            U bestOutput = null;
            long bestScore = Long.MIN_VALUE;

            for (final AlgorithmSpecification<T, U> algorithm : algorithms) {
                final T clonedInput = input.cloneInput();
                final U output = algorithm.solve(clonedInput);
                if (outputValidator != null) {
                    outputValidator.validateOutput(clonedInput, output);
                }
                final long score = scoreCalculator.calculateScore(clonedInput, output);
                if (score > bestScore) {
                    bestScore = score;
                    bestOutput = output;
                }
            }

            final List<String> outputString = outputProducer.produceOutput(bestOutput);
            writeToFile(outputFileLocation, inputFilePath, outputString);
        }
    }

}
