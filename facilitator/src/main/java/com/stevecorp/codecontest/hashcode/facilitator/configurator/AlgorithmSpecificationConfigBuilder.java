package com.stevecorp.codecontest.hashcode.facilitator.configurator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;

public interface AlgorithmSpecificationConfigBuilder {

    AlgorithmOutputValidatorConfigBuilder withAlgorithms(AlgorithmSpecification... algorithms);

}
