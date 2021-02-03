package com.stevecorp.codecontest.hashcode.example.algorithm;

import com.stevecorp.codecontest.hashcode.example.component.Input;
import com.stevecorp.codecontest.hashcode.example.component.Output;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.ParameterizedAlgorithm;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestAlgorithm extends ParameterizedAlgorithm<Input, Output> {

    public static final String PARAMETER_1_KEY = "p1";

    private int parameter1;

    @Override
    public void handleParameters(final Map<String, Object> parameters) {
        this.parameter1 = ((Number) parameters.get(PARAMETER_1_KEY)).intValue();
    }

    @Override
    public Output solve(final Input input) {
        return Output.builder()
                .teamsWithPizzaDelivery(1)
                .deliveries(List.of(
                        Output.Delivery.builder()
                                .teamSize(2)
                                .pizzaIds(Set.of(parameter1, parameter1 + 1))
                                .build()))
                .build();

    }

}
