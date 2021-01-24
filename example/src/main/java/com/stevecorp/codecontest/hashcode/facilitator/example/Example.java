package com.stevecorp.codecontest.hashcode.facilitator.example;

import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.Algorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.ParametrizedAlgorithm;

public class Example {

    public static void main(final String... args) {
        HashCodeFacilitator.configurator()
                .forSpecificInputFiles("example", "little_bit_of_everything")
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
