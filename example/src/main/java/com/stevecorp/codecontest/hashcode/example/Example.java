package com.stevecorp.codecontest.hashcode.example;

import com.stevecorp.codecontest.hashcode.example.algorithm.StevesAmazingAlgorithm;
import com.stevecorp.codecontest.hashcode.example.component.InputParserImpl;
import com.stevecorp.codecontest.hashcode.example.component.OutputProducerImpl;
import com.stevecorp.codecontest.hashcode.example.component.OutputValidatorImpl;
import com.stevecorp.codecontest.hashcode.example.component.ScoreCalculatorImpl;
import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter.BoundedParameter;

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
                                .parameterizedAlgorithm(new StevesAmazingAlgorithm())
                                .withParameters(
                                        new BoundedParameter("p1", 0, 3, 1)
                                )
                                .build()
                )
                .withOutputValidator(new OutputValidatorImpl())
                .withScoreCalculator(new ScoreCalculatorImpl())
                .withOutputProducer(new OutputProducerImpl())
                .withCustomOutputFolder("C:\\Users\\Steve\\Downloads\\hashcode_output")
                .run();
    }

}
