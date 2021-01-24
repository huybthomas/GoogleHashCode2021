package com.stevecorp.codecontest.hashcode.facilitator.config;

public interface InputSpecificationConfigBuilder {

    InputParserConfigBuilder forAllInputFiles();
    InputParserConfigBuilder forASingleInputFile(String inputFile);
    InputParserConfigBuilder forSpecificInputFiles(String... inputFiles);

}
