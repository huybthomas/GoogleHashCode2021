package com.stevecorp.codecontest.hashcode.facilitator.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;

public class FileUtils {

    public static Path getFolderFromResources(final String folderName) {
        try {
            final URL folderURL = FileUtils.class.getClassLoader().getResource(folderName);
            if (folderURL == null) {
                throw new RuntimeException(format(
                        "Failed to retrieve the ''{0}'' folder from the resources folder - make sure it exists!",
                        folderName));
            }
            final Path folderPath = Paths.get(folderURL.toURI());
            if (!Files.isDirectory(folderPath)) {
                throw new RuntimeException(format("''{0}'' is not a folder!", folderName));
            }
            return folderPath;
        } catch (final URISyntaxException e) {
            throw new RuntimeException(format(
                    "Failed to retrieve the ''{0}'' folder from the resources folder!",
                    folderName), e);
        }
    }

    public static Path getFolder(final String fullFolderPath) {
        final Path folderPath = Paths.get(fullFolderPath);
        if (!Files.exists(folderPath)) {
            throw new RuntimeException(format("''{0}'' does not exist!", fullFolderPath));
        }
        if (!Files.isDirectory(folderPath)) {
            throw new RuntimeException(format("''{0}'' is not a folder!", fullFolderPath));
        }
        return folderPath;
    }

    public static List<String> readFileContents(final Path filePath) {
        try {
            return Files.readAllLines(filePath);
        } catch (final Exception e) {
            throw new RuntimeException(format("Unable to read file contents of file ''{0}''",
                    filePath.getFileName().toString()), e);
        }
    }

    public static List<Path> getFilePathsFromFolder(final Path folderPath, final Predicate<String> fileNameMatcher) {
        try {
            return Files.list(folderPath)
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> fileNameMatcher.test(file.getFileName().toString()))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            throw new RuntimeException(format("Unable to read files for folder ''{0}''",
                    folderPath.getFileName().toString()), e);
        }
    }

    public static void writeToFile(final Path destinationFolderPath, final Path inputFile, final List<String> content) {
        try {
            final String inputFileName = inputFile.getFileName().toString();
            final String outputFileName = inputFileName.contains(".")
                    ? inputFileName.substring(0, inputFileName.lastIndexOf(".")) + ".out"
                    : inputFileName + ".out";
            final Path outputFilePath = destinationFolderPath.resolve(outputFileName);
            Files.write(outputFilePath, content);
        } catch (final IOException e) {
            throw new RuntimeException(format("Unable to write algorithm output for input file ''{0}'' to the output folder ''{1}''",
                    inputFile.getFileName().toString(), destinationFolderPath.toString()), e);
        }
    }

}
