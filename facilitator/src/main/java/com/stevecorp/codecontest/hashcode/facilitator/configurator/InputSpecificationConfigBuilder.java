package com.stevecorp.codecontest.hashcode.facilitator.configurator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

public interface InputSpecificationConfigBuilder<T extends InputModel, U extends OutputModel> {

    InputParserConfigBuilder<T, U> forAllInputFiles();
    InputParserConfigBuilder<T, U> forASingleInputFile(String inputFileName);
    InputParserConfigBuilder<T, U> forSpecificInputFiles(String... inputFileNames);

}
