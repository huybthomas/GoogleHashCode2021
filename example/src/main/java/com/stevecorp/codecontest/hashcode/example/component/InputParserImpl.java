package com.stevecorp.codecontest.hashcode.example.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InputParserImpl implements InputParser<Input> {

    @Override
    public Input parseInput(final List<String> input) {

        final int[] inputHeading = Arrays.stream(input.get(0).split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();

        final int numberOfPizzas = inputHeading[0];
        final int numberOf2PersonTeams = inputHeading[1];
        final int numberOf3PersonTeams = inputHeading[2];
        final int numberOf4PersonTeams = inputHeading[3];

        final Map<String, Integer> ingredientIdMapping = new HashMap<>();
        final List<Input.Pizza> pizzas = input.subList(1, input.size()).stream()
                .map(pizzaInputString -> Arrays.asList(pizzaInputString.split(" ")))
                .map(pizzaInput -> Input.Pizza.builder()
                        .numberOfIngredients(Integer.parseInt(pizzaInput.get(0)))
                        .ingredients(mapToIngredientIds(ingredientIdMapping, pizzaInput.subList(1, pizzaInput.size())))
                        .build())
                .collect(Collectors.toList());

        return Input.builder()
                .numberOfPizzas(numberOfPizzas)
                .numberOf2PersonTeams(numberOf2PersonTeams)
                .numberOf3PersonTeams(numberOf3PersonTeams)
                .numberOf4PersonTeams(numberOf4PersonTeams)
                .pizzas(pizzas)
                .build();
    }

    private Set<Integer> mapToIngredientIds(final Map<String, Integer> ingredientIdMapping, final List<String> pizzaInput) {
        return pizzaInput.stream()
                .map(ingredient -> ingredientIdMapping.computeIfAbsent(ingredient, key -> ingredientIdMapping.size()))
                .collect(Collectors.toSet());
    }

}
