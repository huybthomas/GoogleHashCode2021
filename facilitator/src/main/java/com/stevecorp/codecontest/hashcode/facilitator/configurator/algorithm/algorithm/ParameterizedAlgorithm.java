package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.Algorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;

import java.util.Map;

public abstract class ParameterizedAlgorithm<T extends InputModel, U extends OutputModel> implements Algorithm<T, U> {

    public abstract void handleParameters(Map<String, Object> parameters);

}
