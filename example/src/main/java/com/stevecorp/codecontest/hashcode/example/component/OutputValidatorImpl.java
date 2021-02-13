package com.stevecorp.codecontest.hashcode.example.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputValidationException;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputValidator;

import java.util.HashSet;
import java.util.Set;

import static java.text.MessageFormat.format;

public class OutputValidatorImpl implements OutputValidator<Input, Output> {

    @Override
    public void validateOutput(final Input input, final Output output) throws OutputValidationException {

        if (output.teamsWithPizzaDelivery != output.deliveries.size()) {
            throw new OutputValidationException("Mentioned number of deliveries doesn't match with the actual amount of deliveries");
        }

        final Set<Integer> uniquePizzas = new HashSet<>();
        for (final Output.Delivery delivery : output.deliveries) {
            if (delivery.teamSize != delivery.pizzaIds.size()) {
                throw new OutputValidationException(format("Expected {0} pizzas for a team with size {0}, got: {1}",
                        delivery.teamSize, delivery.pizzaIds.size()));
            }
            for (final Integer pizzaId : delivery.pizzaIds) {
                if (uniquePizzas.contains(pizzaId)) {
                    throw new OutputValidationException(format("Pizza with id {0} is used more than once", pizzaId));
                }
                uniquePizzas.add(pizzaId);
            }
        }

    }

}
