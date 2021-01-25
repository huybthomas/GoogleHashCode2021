package com.stevecorp.codecontest.hashcode.example.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.OutputModel;
import lombok.Builder;

import java.util.Set;

@Builder
public class Output implements OutputModel {

    public int teamsWithPizzaDelivery;

    @Builder
    public static final class Delivery {

        public int teamSize;
        public Set<Integer> pizzaIds;

    }

}
