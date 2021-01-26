package com.stevecorp.codecontest.hashcode.example.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public class Output implements OutputModel {

    public int teamsWithPizzaDelivery;
    public List<Delivery> deliveries;

    @Builder
    public static final class Delivery {

        public int teamSize;
        public Set<Integer> pizzaIds;

    }

}
