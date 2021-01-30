package com.stevecorp.codecontest.hashcode.example;

import com.stevecorp.codecontest.hashcode.example.algorithm.SteveBasicAlgorithm;
import com.stevecorp.codecontest.hashcode.example.component.InputParserImpl;
import com.stevecorp.codecontest.hashcode.example.component.OutputProducerImpl;
import com.stevecorp.codecontest.hashcode.example.component.OutputValidatorImpl;
import com.stevecorp.codecontest.hashcode.example.component.ScoreCalculatorImpl;
import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;

public class Example {

    // boundedParameter,enumeratedParameters

    public static void main(final String... args) {
        HashCodeFacilitator.configurator()
                .forSelectedInputFiles("many_ingredients", "many_pizzas")
                .withInputParser(new InputParserImpl())
                .withAlgorithms(new SteveBasicAlgorithm())
                .withOutputValidator(new OutputValidatorImpl())
                .withScoreCalculator(new ScoreCalculatorImpl())
                .withOutputProducer(new OutputProducerImpl())
                .withCustomOutputFolder("C:\\Users\\Steve\\Downloads\\hashcode_output")
                .run();
    }

}
