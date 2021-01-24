package com.stevecorp.codecontest.hashcode.facilitator.util;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.text.MessageFormat.format;

public class FileUtil {

    public static Path getFolderFromResources(final String folderName) {
        try {
            final URL folderURL = FileUtil.class.getClassLoader().getResource(folderName);
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

}
