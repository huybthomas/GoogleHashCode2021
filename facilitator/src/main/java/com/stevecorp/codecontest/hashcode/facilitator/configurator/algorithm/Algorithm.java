package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;

public interface Algorithm<T extends InputModel, U extends OutputModel> {

    void preAlgorithmSetup();
    U solve(T input);

}
