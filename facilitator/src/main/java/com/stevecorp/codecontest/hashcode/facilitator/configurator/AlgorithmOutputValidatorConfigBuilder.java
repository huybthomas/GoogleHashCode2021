package com.stevecorp.codecontest.hashcode.facilitator.configurator;

public interface AlgorithmOutputValidatorConfigBuilder {

    ScoreCalculatorConfigBuilder dontValidateAlgorithmOutput();
    ScoreCalculatorConfigBuilder withAlgorithmOutputValidator(Object algorithmOutputValidator);

}
