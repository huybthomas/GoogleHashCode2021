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
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.stevecorp.codecontest.hashcode.example.algorithm.StevesAmazingAlgorithm.PizzaIngredientScoreOperation.SUM;

public class StevesAmazingAlgorithm extends ParameterizedAlgorithm<Input, Output> {

    public static final String PARAMETER_COMMON_INGREDIENT_PUNISHMENT_FACTOR = "P1";
    public static final String PARAMETER_PIZZA_INGREDIENT_SCORE_OPERATOR = "P2";

    private long ingredientPunishmentFactor;
    private PizzaIngredientScoreOperation pizzaIngredientScoreOperation;

    @Override
    public void handleParameters(final Map<String, Object> parameters) {
        this.ingredientPunishmentFactor = (long) parameters.get(PARAMETER_COMMON_INGREDIENT_PUNISHMENT_FACTOR);
        this.pizzaIngredientScoreOperation = (PizzaIngredientScoreOperation) parameters.get(PARAMETER_PIZZA_INGREDIENT_SCORE_OPERATOR);
        System.out.println(ingredientPunishmentFactor + " - " + pizzaIngredientScoreOperation);
    }

    @Override
    public Output solve(final Input input) {

        final Map<Integer, Long> ingredientCounts = input.pizzas.stream()
                .map(pizza -> pizza.ingredients)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(value -> value, Collectors.counting()));
        final double averageIngredientCount = ingredientCounts.values().stream()
                .mapToLong(value -> value)
                .average().orElseThrow(() -> new RuntimeException("Could not calculate average ingredient count"));
        final Map<Integer, Double> percentualIngredientOccurrences = ingredientCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (1.0 * entry.getValue() / averageIngredientCount) * 100
                ));

        final List<Input.Pizza> sortedPizzas = input.pizzas.stream()
                .map(pizza -> new AbstractMap.SimpleEntry<>(pizza.id, getIngredientScore(pizza, percentualIngredientOccurrences)))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(pizzaScoreEntry -> input.pizzas.get(pizzaScoreEntry.getKey()))
                .collect(Collectors.toList());

        final int numberOfPizzasForAllTeams = 2 * input.numberOf2PersonTeams + 3 * input.numberOf3PersonTeams + 4 * input.numberOf4PersonTeams;
        return input.numberOfPizzas >= numberOfPizzasForAllTeams
                ? solveForAbundanceOfPizzas(input, sortedPizzas)
                : solveForLackOfPizzas(input, sortedPizzas);

    }

    private double getIngredientScore(final Input.Pizza pizza, final Map<Integer, Double> percentualIngredientOccurrences) {
        final DoubleStream ingredientCountStream = pizza.ingredients.stream()
                .mapToDouble(ingredient -> ingredientPunishmentFactor / percentualIngredientOccurrences.get(ingredient));
        return pizzaIngredientScoreOperation == SUM ? ingredientCountStream.sum()
                : ingredientCountStream.reduce(1, (a, b) -> a * b);
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

    public enum PizzaIngredientScoreOperation {
        SUM, MULTIPLICATION
    }

}
