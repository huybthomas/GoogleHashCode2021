package com.stevecorp.codecontest.hashcode.facilitator.configurator;

public interface InputSpecificationConfigBuilder {

    InputParserConfigBuilder forAllInputFiles();
    InputParserConfigBuilder forASingleInputFile(String inputFileName);
    InputParserConfigBuilder forSpecificInputFiles(String... inputFileNames);

}
