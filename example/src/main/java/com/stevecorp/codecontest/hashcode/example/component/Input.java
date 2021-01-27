package com.stevecorp.codecontest.hashcode.example.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder(toBuilder = true)
public class Input implements InputModel {

    public int numberOfPizzas;
    public int numberOf2PersonTeams;
    public int numberOf3PersonTeams;
    public int numberOf4PersonTeams;
    public List<Pizza> pizzas;

    @Getter
    @Setter
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
