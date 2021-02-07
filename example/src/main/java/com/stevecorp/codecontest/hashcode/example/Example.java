package com.stevecorp.codecontest.hashcode.example;

import com.stevecorp.codecontest.hashcode.example.algorithm.SteveBasicAlgorithm;
import com.stevecorp.codecontest.hashcode.example.algorithm.TestAlgorithm;
import com.stevecorp.codecontest.hashcode.example.component.InputParserImpl;
import com.stevecorp.codecontest.hashcode.example.component.OutputProducerImpl;
import com.stevecorp.codecontest.hashcode.example.component.OutputValidatorImpl;
import com.stevecorp.codecontest.hashcode.example.component.ScoreCalculatorImpl;
import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter.BoundedParameter;

import static com.stevecorp.codecontest.hashcode.example.algorithm.TestAlgorithm.PARAMETER_1_KEY;

public class Example {

    public static void main(final String... args) {
        HashCodeFacilitator.configurator()
                .forSelectedInputFiles(
                        "many_pizzas",
                        "many_teams"
                )
                .withInputParser(new InputParserImpl())
                .withAlgorithms(
                        AlgorithmSpecification.builder()
                                .basicAlgorithm(new SteveBasicAlgorithm())
                                .build(),
                        AlgorithmSpecification.builder()
                                .parameterizedAlgorithm(new TestAlgorithm())
                                .withParameters(
                                        new BoundedParameter(PARAMETER_1_KEY, 0, 10, 1))
                                .build()
                )
                .withOutputValidator(new OutputValidatorImpl())
                .withScoreCalculator(new ScoreCalculatorImpl())
                .withOutputProducer(new OutputProducerImpl())
                .withCustomOutputFolder("C:\\Users\\Steve\\Downloads\\hashcode_output")
                .run();
    }

}
