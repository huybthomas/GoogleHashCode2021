package com.stevecorp.codecontest.hashcode.facilitator.config;

public interface AlgorithmOutputValidatorConfigBuilder {

    ScoreCalculatorConfigBuilder dontValidateAlgorithmOutput();
    ScoreCalculatorConfigBuilder withAlgorithmOutputValidator(Object algorithmOutputValidator);

}
