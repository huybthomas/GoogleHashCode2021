package com.stevecorp.codecontest.hashcode.hashcode2021.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.BasicAlgorithm;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Input;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Ace extends BasicAlgorithm<Input, Output> {

    public static final String RELATIVE_INTERSECTION_DURATION_MULTIPLIER = "RELATIVE_INTERSECTION_DURATION_MULTIPLIER";
    private long relativeIntersectionDurationMultiplier = 3;
    private double relativeIntersectionSizeMultiplier = 0.2;
    private double maxTime;

    @Override
    public Output solve(final Input input) {
        maxTime = input.simulationDurationSeconds;
//            input.simulationDurationSeconds;
//            input.numberOfIntersections;
//            input.numberOfStreets;
//            input.numberOfCars;
//            input.bonusPointsForReachingDestinationInTime;
//            input.streets;
//            input.carPaths;

        Map<Integer, Input.Street> streetMap = input.streets.stream().collect(Collectors.toMap(street -> street.streetId, street -> street));

        List<ExtendedCarPath> extendedCarPaths = parseCarPaths(streetMap, input.carPaths);
        Map<Integer, ExtendedStreet> extendedStreet = parseStreets(streetMap, input.carPaths);
        Map<Integer, Intersection> intersections = parseIntersections(extendedStreet);

        List<Output.IntersectionSchedule> schedules = parse(intersections, extendedCarPaths, extendedStreet);

        return Output.builder()
                .numberOfIntersectionsSchedules(schedules.size())
                .intersectionSchedules(schedules)
                .build();
    }

    private Map<Integer, Intersection> parseIntersections(Map<Integer, ExtendedStreet> extendedStreets) {
        Map<Integer, Intersection> intersections = new HashMap<>();

        for (Map.Entry<Integer, ExtendedStreet> entry : extendedStreets.entrySet()) {
            ExtendedStreet extendedStreet = entry.getValue();
            Input.Street street = extendedStreet.street;
            Intersection startIntersection = intersections.get(street.startIntersection);
            if(startIntersection == null) {
                startIntersection = new Intersection(street.startIntersection);
                intersections.put(street.startIntersection, startIntersection);
            }
            Intersection endIntersection = intersections.get(street.endIntersection);
            if(endIntersection == null) {
                endIntersection = new Intersection(street.endIntersection);
                intersections.put(street.endIntersection, endIntersection);
            }

            startIntersection.outputs.add(extendedStreet);
            endIntersection.inputs.add(extendedStreet);
        }

        return intersections;
    }

    private List<Output.IntersectionSchedule> parse(Map<Integer, Intersection> intersections, List<ExtendedCarPath> extendedCarPaths, Map<Integer, ExtendedStreet> extendedStreet) {
        //Determine intersection schedule based on number of expected cars
        return intersections.entrySet().stream()
                .map(entry -> computeToIntersectionSchedule(entry.getValue()))
                .filter(intersectionSchedule -> intersectionSchedule.numberOfIncomingStreets > 0).collect(Collectors.toList());
    }

    private Output.IntersectionSchedule computeToIntersectionSchedule(Intersection intersection) {
        List<ExtendedStreet> inputs = intersection.inputs;
        long max = inputs.stream().mapToLong(extendedStreet -> extendedStreet.totalNumCars).max().getAsLong();
        Map<ExtendedStreet, Integer> relativeStreetTimingForIntersection = inputs.stream().collect(Collectors.toMap(extendedStreet -> extendedStreet, extendedStreet -> {
            var normalizedWeight = (double) extendedStreet.totalNumCars / (double) max;
            var timingForIntersection = normalizedWeight * ((intersection.inputs.size() + intersection.outputs.size()) * relativeIntersectionSizeMultiplier);
            return (int) Math.ceil(timingForIntersection * relativeIntersectionDurationMultiplier);
        }));
        List<Output.GreenLightDuration> durations = inputs.stream().map(extendedStreet -> Output.GreenLightDuration.builder()
                .streetName(extendedStreet.street.streetName)
                .greenLightDuration(relativeStreetTimingForIntersection.get(extendedStreet))
                .build()).collect(Collectors.toList());

        List<Output.GreenLightDuration> onlyValidDurations = durations.stream().filter(greenLightDuration -> greenLightDuration.greenLightDuration >= 1 && greenLightDuration.greenLightDuration < maxTime).collect(Collectors.toList());

        return Output.IntersectionSchedule.builder()
                .intersectionId(intersection.id)
                .numberOfIncomingStreets(onlyValidDurations.size())
                .greenLightDurations(onlyValidDurations)
                .build();
    }

    private Map<Integer, ExtendedStreet> parseStreets(Map<Integer, Input.Street> streetMap, List<Input.CarPath> carPaths) {
        //Num cars on street
        AtomicInteger i = new AtomicInteger();
        return streetMap.entrySet().stream().parallel()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    Input.Street street = entry.getValue();
                    ExtendedStreet extendedStreet = new ExtendedStreet(street);

                    AtomicInteger j = new AtomicInteger();
                    extendedStreet.totalNumCars = carPaths.stream()
                            .map(Input.CarPath::getStreetIds)
                            .mapToLong(streetIds -> streetIds.stream().filter(id -> id.equals(street.streetId)).count()).sum();

                    return extendedStreet;
                }));

    }

    private List<ExtendedCarPath> parseCarPaths(Map<Integer, Input.Street> streetMap, List<Input.CarPath> carPaths) {
        return carPaths.stream().map(carPath -> {
            ExtendedCarPath extendedCarPath = new ExtendedCarPath(carPath);
            //total length
            extendedCarPath.totalLength = carPath.streetIds.stream()
                    .map(streetMap::get)
                    .mapToInt(street -> street.timeToGetFromStartToEnd)
                    .sum();
            return extendedCarPath;
        }).collect(Collectors.toList());
    }

    private class ExtendedCarPath {
        final Input.CarPath carPath;

        int totalLength;

        private ExtendedCarPath(Input.CarPath carPath) {
            this.carPath = carPath;
        }
    }

    private class ExtendedStreet {
        final Input.Street street;
        public long totalNumCars;

        private ExtendedStreet(Input.Street street) {
            this.street = street;
        }
    }

    private class Intersection {
        int id;
        final List<ExtendedStreet> inputs = new ArrayList<>();
        final List<ExtendedStreet> outputs = new ArrayList<>();

        public Intersection(int id) {
            this.id = id;
        }
    }
}
