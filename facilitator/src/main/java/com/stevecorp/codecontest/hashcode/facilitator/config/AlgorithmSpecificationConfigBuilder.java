package com.stevecorp.codecontest.hashcode.facilitator.config;

import com.stevecorp.codecontest.hashcode.facilitator.config.algorithm.AlgorithmSpecification;

public interface AlgorithmSpecificationConfigBuilder {

    AlgorithmOutputValidatorConfigBuilder withAlgorithms(AlgorithmSpecification... algorithms);

}
