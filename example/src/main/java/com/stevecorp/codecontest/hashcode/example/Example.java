package com.stevecorp.codecontest.hashcode.example;

import com.stevecorp.codecontest.hashcode.example.component.InputParserImpl;
import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.Algorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.ParametrizedAlgorithm;

public class Example {

    public static void main(final String... args) {
        HashCodeFacilitator.configurator()
                .forSpecificInputFiles("many_pizzas")
                .withInputParser(new InputParserImpl())
                .withAlgorithms(
                        new Algorithm(),
                        new ParametrizedAlgorithm())
                .withAlgorithmOutputValidator("outputValidator")
                .withScoreCalculator("scoreCalculator")
                .withOutputProducer("outputProducer")
                .run();
    }

}
