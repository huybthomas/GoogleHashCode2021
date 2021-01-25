package com.stevecorp.codecontest.hashcode.example.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.score.ScoreCalculator;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreCalculatorImpl implements ScoreCalculator<Input, Output> {

    @Override
    public long calculateScore(final Input input, final Output output) {
        return output.deliveries.stream()
                .map(delivery -> delivery.pizzaIds)
                .map(pizzaIds -> mapPizzaIdsToIngredientIds(input.pizzas, pizzaIds))
                .mapToLong(uniqueIngredients -> (long) Math.pow(uniqueIngredients.size(), 2))
                .sum();
    }

    private Set<Integer> mapPizzaIdsToIngredientIds(final List<Input.Pizza> pizzas, final Set<Integer> pizzaIds) {
        return pizzaIds.stream()
                .map(pizzaId -> pizzas.get(pizzaId).ingredients)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
