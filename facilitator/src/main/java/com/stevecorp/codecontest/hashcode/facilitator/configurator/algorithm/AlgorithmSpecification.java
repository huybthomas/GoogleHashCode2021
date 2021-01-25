package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

public interface AlgorithmSpecification<T extends InputModel, U extends OutputModel> {

    U solve(T input);

    default String getAlgorithmName() {
        return this.getClass().getSimpleName();
    }

}
