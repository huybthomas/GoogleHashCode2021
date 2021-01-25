package com.stevecorp.codecontest.hashcode.example.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OutputProducerImpl implements OutputProducer<Output> {

    @Override
    public List<String> produceOutput(final Output output) {
        final List<String> outputString = new ArrayList<>();

        outputString.add(String.valueOf(output.teamsWithPizzaDelivery));
        output.deliveries.stream()
                .map(this::deliveryToString)
                .forEach(outputString::add);

        return outputString;
    }

    private String deliveryToString(final Output.Delivery delivery) {
        return delivery.teamSize + delivery.pizzaIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

}
