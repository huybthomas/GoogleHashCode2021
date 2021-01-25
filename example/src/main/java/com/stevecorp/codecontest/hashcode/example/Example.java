package com.stevecorp.codecontest.hashcode.example;

import com.stevecorp.codecontest.hashcode.example.algorithm.SteveGorithmNoParams;
import com.stevecorp.codecontest.hashcode.example.algorithm.SteveGorithmParams;
import com.stevecorp.codecontest.hashcode.example.component.InputParserImpl;
import com.stevecorp.codecontest.hashcode.example.component.OutputValidatorImpl;
import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;

public class Example {

    public static void main(final String... args) {
        HashCodeFacilitator.configurator()
                .forSpecificInputFiles("many_pizzas")
                .withInputParser(new InputParserImpl())
                .withAlgorithms(
                        new SteveGorithmParams(),
                        new SteveGorithmNoParams())
                .withOutputValidator(new OutputValidatorImpl())
                .withScoreCalculator("scoreCalculator")
                .withOutputProducer("outputProducer")
                .run();
    }

}
