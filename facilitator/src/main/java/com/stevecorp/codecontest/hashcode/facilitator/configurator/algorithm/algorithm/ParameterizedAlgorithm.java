package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.Algorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;

import java.util.Map;

/**
 * Abstract base class for parameterized algorithms.
 *
 * Besides implementing the `U solve(T)` method, algorithms that extend this algorithm class will also need to
 *  implement logic to handle parameter updates through the `handleParameters(Map<String, Object>)` method.
 */
public abstract class ParameterizedAlgorithm<T extends InputModel, U extends OutputModel> implements Algorithm<T, U> {

    /**
     * The method that handles the parameter refresh.
     *
     * @param parameters the new set of parameters for the algorithm.
     */
    public abstract void handleParameters(Map<String, Object> parameters);

}
