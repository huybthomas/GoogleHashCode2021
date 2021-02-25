package com.stevecorp.codecontest.hashcode.hashcode2021.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputParserImpl implements InputParser<Input> {

    @Override
    public Input parseInput(final List<String> input) {

        final int[] inputHeading = Arrays.stream(input.get(0).split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();

        int currentId = 0;
        final Map<String, Integer> streetIdMapping = new HashMap<>();
        final List<Input.Street> streets = new ArrayList<>();
        for(final String streetInput : input.subList(1, inputHeading[2] + 1)) {
            final String[] streetInputElements = streetInput.split(" ");
            final int startIntersection = Integer.parseInt(streetInputElements[0]);
            final int endIntersection = Integer.parseInt(streetInputElements[1]);
            final String streetName = streetInputElements[2];
            final int timeToGetFromStartToEnd = Integer.parseInt(streetInputElements[3]);
            if (!streetIdMapping.containsKey(streetName)) {
                streetIdMapping.put(streetName, currentId++);
            }
            final int streetId = streetIdMapping.get(streetName);
            streets.add(new Input.Street(streetId, startIntersection, endIntersection, timeToGetFromStartToEnd));
        }

        final List<Input.CarPath> carPaths = new ArrayList<>();
        for(final String carPathInput : input.subList(1 + inputHeading[2], input.size())) {
            final String[] carPathInputElements = carPathInput.split(" ");
            final int numberOfStreets = Integer.parseInt(carPathInputElements[0]);
            final List<Integer> streetIds = new ArrayList<>();
            for (int streetIndex = 1; streetIndex <= numberOfStreets; streetIndex++) {
                final String streetName = carPathInputElements[streetIndex];
                streetIds.add(streetIdMapping.get(streetName));
            }
            carPaths.add(new Input.CarPath(numberOfStreets, streetIds));
        }

        return Input.builder()
                .simulationDurationSeconds(inputHeading[0])
                .numberOfIntersections(inputHeading[1])
                .numberOfStreets(inputHeading[2])
                .numberOfCars(inputHeading[3])
                .bonusPointsForReachingDestinationInTime(inputHeading[4])
                .streets(streets)
                .carPaths(carPaths)
                .build();
    }

}
