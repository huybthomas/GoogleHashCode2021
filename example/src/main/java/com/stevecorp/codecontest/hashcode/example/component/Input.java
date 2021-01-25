package com.stevecorp.codecontest.hashcode.example.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import lombok.Builder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public class Input implements InputModel {

    public int numberOfPizzas;
    public int numberOf2PersonTeams;
    public int numberOf3PersonTeams;
    public int numberOf4PersonTeams;
    public List<Pizza> pizzas;

    @Builder(toBuilder = true)
    public static final class Pizza {

        public int id;
        public int numberOfIngredients;
        public Set<Integer> ingredients;

    }

    @Override
    public Input cloneInput() {
        return this.toBuilder()
                .pizzas(this.pizzas.stream()
                        .map(pizza -> pizza.toBuilder().build())
                        .collect(Collectors.toList()))
                .build();
    }

}
