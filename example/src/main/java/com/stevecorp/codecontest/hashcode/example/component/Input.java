package com.stevecorp.codecontest.hashcode.example.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public class Input implements InputModel {

    public int numberOfPizzas;
    public int numberOf2PersonTeams;
    public int numberOf3PersonTeams;
    public int numberOf4PersonTeams;
    public List<Pizza> pizzas;

    @Builder
    public static final class Pizza {

        public int id;
        public int numberOfIngredients;
        public Set<Integer> ingredients;

    }

}
