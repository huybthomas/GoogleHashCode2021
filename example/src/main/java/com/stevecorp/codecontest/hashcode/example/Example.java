package com.stevecorp.codecontest.hashcode.example;

import com.stevecorp.codecontest.hashcode.example.algorithm.StevesAmazingAlgorithm;
import com.stevecorp.codecontest.hashcode.example.component.InputParserImpl;
import com.stevecorp.codecontest.hashcode.example.component.OutputProducerImpl;
import com.stevecorp.codecontest.hashcode.example.component.OutputValidatorImpl;
import com.stevecorp.codecontest.hashcode.example.component.ScoreCalculatorImpl;
import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter.BoundedParameter;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter.EnumeratedParameter;

import static com.stevecorp.codecontest.hashcode.example.algorithm.StevesAmazingAlgorithm.PARAMETER_COMMON_INGREDIENT_PUNISHMENT_FACTOR;
import static com.stevecorp.codecontest.hashcode.example.algorithm.StevesAmazingAlgorithm.PARAMETER_PIZZA_INGREDIENT_SCORE_OPERATOR;
import static com.stevecorp.codecontest.hashcode.example.algorithm.StevesAmazingAlgorithm.PizzaIngredientScoreOperation.MULTIPLICATION;
import static com.stevecorp.codecontest.hashcode.example.algorithm.StevesAmazingAlgorithm.PizzaIngredientScoreOperation.SUM;

public class Example {

    public static void main(final String... args) {
        HashCodeFacilitator.configurator()
                .forAllInputFiles()
                .withInputParser(new InputParserImpl())
                .withAlgorithms(
                        AlgorithmSpecification.builder()
                                .parameterizedAlgorithm(new StevesAmazingAlgorithm())
                                .withParameters(
                                        new BoundedParameter(PARAMETER_COMMON_INGREDIENT_PUNISHMENT_FACTOR, 100, 5000, 100),
                                        new EnumeratedParameter(PARAMETER_PIZZA_INGREDIENT_SCORE_OPERATOR, SUM, MULTIPLICATION)
                                )
                                .build()
                )
                .withOutputValidator(new OutputValidatorImpl())
                .withScoreCalculator(new ScoreCalculatorImpl())
                .withOutputProducer(new OutputProducerImpl())
                .run();
    }

}
