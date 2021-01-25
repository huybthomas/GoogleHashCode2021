package com.stevecorp.codecontest.hashcode.facilitator.configurator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

public interface AlgorithmSpecificationConfigBuilder<T extends InputModel, U extends OutputModel> {

    OutputValidatorConfigBuilder<T, U> withAlgorithms(AlgorithmSpecification<T, U>... algorithms);

}
