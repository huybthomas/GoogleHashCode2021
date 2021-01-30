package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.Algorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;

public abstract class BasicAlgorithm<T extends InputModel, U extends OutputModel> implements Algorithm<T, U> {

    @Override
    public void preAlgorithmSetup() {}

}
