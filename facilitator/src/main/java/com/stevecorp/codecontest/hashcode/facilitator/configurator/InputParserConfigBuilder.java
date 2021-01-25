package com.stevecorp.codecontest.hashcode.facilitator.configurator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputParser;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

public interface InputParserConfigBuilder<T extends InputModel, U extends OutputModel> {

    AlgorithmSpecificationConfigBuilder<T, U> withInputParser(InputParser<T> inputParser);

}
