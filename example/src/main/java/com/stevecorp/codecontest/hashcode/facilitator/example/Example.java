package com.stevecorp.codecontest.hashcode.facilitator.example;

import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;
import com.stevecorp.codecontest.hashcode.facilitator.config.algorithm.Algorithm;
import com.stevecorp.codecontest.hashcode.facilitator.config.algorithm.ParametrizedAlgorithm;

public class Example {

    public static void main(final String... args) {
        HashCodeFacilitator.configurator()
                .forSpecificInputFiles("a", "b")
                .withInputParser("inputParser")
                .withAlgorithms(
                        new Algorithm(),
                        new ParametrizedAlgorithm())
                .withAlgorithmOutputValidator("outputValidator")
                .withScoreCalculator("scoreCalculator")
                .withOutputProducer("outputProducer")
                .run();
    }

}
