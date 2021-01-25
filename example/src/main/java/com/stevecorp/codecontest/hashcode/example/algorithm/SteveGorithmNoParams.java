package com.stevecorp.codecontest.hashcode.example.algorithm;

import com.stevecorp.codecontest.hashcode.example.component.Input;
import com.stevecorp.codecontest.hashcode.example.component.Output;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.Algorithm;

import java.util.List;
import java.util.Set;

public class SteveGorithmNoParams extends Algorithm<Input, Output> {

    @Override
    public Output solve(final Input input) {
        return Output.builder()
                .teamsWithPizzaDelivery(5)
                .deliveries(List.of(
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(0, 1, 2)).build(),
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(3, 4, 5)).build(),
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(6, 7, 8)).build(),
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(9, 10, 11)).build(),
                        Output.Delivery.builder().teamSize(3).pizzaIds(Set.of(12, 13, 14)).build()
                ))
                .build();
    }

}
