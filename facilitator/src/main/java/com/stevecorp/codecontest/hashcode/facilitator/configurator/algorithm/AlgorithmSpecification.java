package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;

public interface AlgorithmSpecification<T extends InputModel, U extends OutputModel> {

    U solve(T input);

}
