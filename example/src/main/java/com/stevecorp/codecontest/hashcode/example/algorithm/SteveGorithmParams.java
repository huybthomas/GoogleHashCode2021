package com.stevecorp.codecontest.hashcode.example.algorithm;

import com.stevecorp.codecontest.hashcode.example.component.Input;
import com.stevecorp.codecontest.hashcode.example.component.Output;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.ParametrizedAlgorithm;

import java.util.List;
import java.util.Set;

public class SteveGorithmParams extends ParametrizedAlgorithm<Input, Output> {

    @Override
    public Output solve(final Input input) {
        return Output.builder()
                .teamsWithPizzaDelivery(5)
                .deliveries(List.of(
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(10, 11, 12)).build(),
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(13, 14, 15)).build(),
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(16, 17, 18)).build(),
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(19, 110, 111)).build(),
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(112, 113, 114)).build()
                ))
                .build();
    }

}
