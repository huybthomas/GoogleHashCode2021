package com.stevecorp.codecontest.hashcode.example.algorithm;

import com.stevecorp.codecontest.hashcode.example.component.Input;
import com.stevecorp.codecontest.hashcode.example.component.Output;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.ParameterizedAlgorithm;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StevesAmazingAlgorithm extends ParameterizedAlgorithm<Input, Output> {

    @Override
    public void handleParameters(final Map<String, Object> parameters) {
    }

    @Override
    public Output solve(final Input input) {

        final long[] ingredientCounter = { 0 };
        final Map<Integer, Long> ingredientCounts = input.pizzas.stream()
                .map(pizza -> pizza.ingredients)
                .peek(ingredients -> ingredientCounter[0] += ingredients.size())
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(value -> value, Collectors.counting()));

        final List<Input.Pizza> sortedPizzas = input.pizzas.stream()
                .map(pizza -> new AbstractMap.SimpleEntry<>(pizza.id, getIngredientScore(pizza, ingredientCounts, ingredientCounter[0])))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(pizzaScoreEntry -> input.pizzas.get(pizzaScoreEntry.getKey()))
                .collect(Collectors.toList());

        final int numberOfPizzasForAllTeams = 2 * input.numberOf2PersonTeams + 3 * input.numberOf3PersonTeams + 4 * input.numberOf4PersonTeams;
        return input.numberOfPizzas >= numberOfPizzasForAllTeams
                ? solveForAbundanceOfPizzas(input, sortedPizzas)
                : solveForLackOfPizzas(input, sortedPizzas);

    }

    private long getIngredientScore(final Input.Pizza pizza, final Map<Integer, Long> ingredientCounts, final long totalNumberOfIngredients) {
        return pizza.ingredients.stream()
                .mapToLong(ingredient -> totalNumberOfIngredients - ingredientCounts.get(ingredient))
                .sum();
    }

    private Output solveForAbundanceOfPizzas(final Input input, final List<Input.Pizza> sortedPizzas) {

        final List<Output.Delivery> deliveries2 = new ArrayList<>(input.numberOf2PersonTeams);
        final List<Output.Delivery> deliveries3 = new ArrayList<>(input.numberOf3PersonTeams);
        final List<Output.Delivery> deliveries4 = new ArrayList<>(input.numberOf4PersonTeams);

        IntStream.range(0, input.numberOf2PersonTeams)
                .mapToObj(index -> sortedPizzas.remove(0))
                .forEach(pizza -> deliveries2.add(Output.Delivery.builder().teamSize(2).pizzaIds(toMutableSet(pizza.id)).build()));
        IntStream.range(0, input.numberOf3PersonTeams)
                .mapToObj(index -> sortedPizzas.remove(0))
                .forEach(pizza -> deliveries3.add(Output.Delivery.builder().teamSize(3).pizzaIds(toMutableSet(pizza.id)).build()));
        IntStream.range(0, input.numberOf4PersonTeams)
                .mapToObj(index -> sortedPizzas.remove(0))
                .forEach(pizza -> deliveries4.add(Output.Delivery.builder().teamSize(4).pizzaIds(toMutableSet(pizza.id)).build()));

        IntStream.range(0, input.numberOf2PersonTeams)
                .forEach(index -> deliveries2.get(index).pizzaIds.add(sortedPizzas.remove(0).id));
        IntStream.range(0, input.numberOf3PersonTeams)
                .forEach(index -> deliveries3.get(index).pizzaIds.add(sortedPizzas.remove(0).id));
        IntStream.range(0, input.numberOf4PersonTeams)
                .forEach(index -> deliveries4.get(index).pizzaIds.add(sortedPizzas.remove(0).id));

        IntStream.range(0, input.numberOf3PersonTeams)
                .forEach(index -> deliveries3.get(index).pizzaIds.add(sortedPizzas.remove(0).id));
        IntStream.range(0, input.numberOf4PersonTeams)
                .forEach(index -> deliveries4.get(index).pizzaIds.add(sortedPizzas.remove(0).id));

        IntStream.range(0, input.numberOf4PersonTeams)
                .forEach(index -> deliveries4.get(index).pizzaIds.add(sortedPizzas.remove(0).id));

        return Output.builder()
                .teamsWithPizzaDelivery(deliveries2.size() + deliveries3.size() + deliveries4.size())
                .deliveries(Stream.of(deliveries2, deliveries3, deliveries4)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .build();

    }

    private Output solveForLackOfPizzas(final Input input, final List<Input.Pizza> sortedPizzas) {

        final List<Output.Delivery> deliveries2 = new ArrayList<>(input.numberOf2PersonTeams);
        final List<Output.Delivery> deliveries3 = new ArrayList<>(input.numberOf3PersonTeams);
        final List<Output.Delivery> deliveries4 = new ArrayList<>(input.numberOf4PersonTeams);

        try {

            IntStream.range(0, input.getNumberOf2PersonTeams())
                    .forEach(index -> deliveries2.add(Output.Delivery.builder()
                            .teamSize(2)
                            .pizzaIds(Set.of(
                                    sortedPizzas.remove(0).id,
                                    sortedPizzas.remove(sortedPizzas.size() - 1).id))
                            .build()));
            IntStream.range(0, input.getNumberOf3PersonTeams())
                    .forEach(index -> deliveries3.add(Output.Delivery.builder()
                            .teamSize(3)
                            .pizzaIds(Set.of(
                                    sortedPizzas.remove(0).id,
                                    sortedPizzas.remove(sortedPizzas.size() - 1).id,
                                    sortedPizzas.remove(sortedPizzas.size() - 1).id))
                            .build()));
            IntStream.range(0, input.getNumberOf4PersonTeams())
                    .forEach(index -> deliveries4.add(Output.Delivery.builder()
                            .teamSize(4)
                            .pizzaIds(Set.of(
                                    sortedPizzas.remove(0).id,
                                    sortedPizzas.remove(sortedPizzas.size() - 1).id,
                                    sortedPizzas.remove(sortedPizzas.size() - 1).id,
                                    sortedPizzas.remove(sortedPizzas.size() - 1).id))
                            .build()));

        } catch (final Exception e) {
            // ignore
        }

        return Output.builder()
                .teamsWithPizzaDelivery(deliveries2.size() + deliveries3.size() + deliveries4.size())
                .deliveries(Stream.of(deliveries2, deliveries3, deliveries4)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .build();

    }

    private Set<Integer> toMutableSet(final Integer pizzaId) {
        final Set<Integer> pizzaIds = new HashSet<>();
        pizzaIds.add(pizzaId);
        return pizzaIds;
    }

}
