package com.stevecorp.codecontest.hashcode.hashcode2021.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.ParameterizedAlgorithm;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Input;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Ace extends ParameterizedAlgorithm<Input, Output> {

    public static final String PARAMETER_1_NAME = "P1";
    public static final String PARAMETER_2_NAME = "P2";

    private long parameter1Value;
    private String parameter2Value;

    @Override
    public void handleParameters(final Map<String, Object> parameters) {
        parameter1Value = (long) parameters.get(PARAMETER_1_NAME);
        parameter2Value = (String) parameters.get(PARAMETER_2_NAME);
    }

    @Override
    public Output solve(final Input input) {
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

        return Output.builder()
//                .
                .build();
    }

    private Map<Integer, ExtendedStreet> parseStreets(Map<Integer, Input.Street> streetMap, List<Input.CarPath> carPaths) {
        //Num cars on street
        return streetMap.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> {
                    Input.Street street = entry.getValue();
                    ExtendedStreet extendedStreet = new ExtendedStreet(street);

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
}
