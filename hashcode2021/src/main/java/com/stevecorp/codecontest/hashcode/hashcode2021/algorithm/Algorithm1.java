package com.stevecorp.codecontest.hashcode.hashcode2021.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.ParameterizedAlgorithm;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Input;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output;

import java.util.Map;

public class Algorithm1 extends ParameterizedAlgorithm<Input, Output> {

    public static final String PARAMETER_1_NAME = "P1";
    public static final String PARAMETER_2_NAME = "P2";

    private long parameter1Value;
    private String parameter2Value;

    @Override
    public void handleParameters(final Map<String, Object> parameters) {
        parameter1Value = (long) parameters.get(PARAMETER_1_NAME);
        parameter2Value = (String) parameters.get(PARAMETER_2_NAME);
    }

    @Override
    public Output solve(final Input input) {
        return Output.builder().build();
    }

}
