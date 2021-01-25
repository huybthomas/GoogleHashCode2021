package com.stevecorp.codecontest.hashcode.facilitator.configurator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputParser;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;

public interface InputParserConfigBuilder {

    AlgorithmSpecificationConfigBuilder withInputParser(InputParser<? extends InputModel> inputParser);

}
