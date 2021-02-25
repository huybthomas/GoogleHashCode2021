package com.stevecorp.codecontest.hashcode.hashcode2021.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.ParameterizedAlgorithm;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Input;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Input.CarPath;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Input.Street;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output.GreenLightDuration;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output.IntersectionSchedule;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithm1 extends ParameterizedAlgorithm<Input, Output> {

    public static final String PARAMETER_1_NAME = "P1";
    public static final String PARAMETER_2_NAME = "P2";

    private long parameter1Value;
    private String parameter2Value;

    public Map<Integer, StreetExt> usedStreets = new LinkedHashMap<>();
    public Map<Integer, StreetExt> unusedStreets = new LinkedHashMap();
    public Map<Integer, StreetExt> streets = new LinkedHashMap<>();
    public Map<Integer, Intersection> usedIntersectionMap = new LinkedHashMap<>();
    public Map<Integer, Intersection> unusedIntersectionMap = new LinkedHashMap<>();

    @Override
    public void handleParameters(final Map<String, Object> parameters) {
        parameter1Value = (long) parameters.get(PARAMETER_1_NAME);
        parameter2Value = (String) parameters.get(PARAMETER_2_NAME);
    }

    @Override
    public Output solve(final Input input) {
        for (Street street : input.getStreets()) {
            streets.put(street.getStreetId(), new StreetExt(street));
        }

        for (CarPath carPath : input.carPaths) {
            for (Integer streetId : carPath.streetIds) {
                StreetExt street= streets.get(streetId);
                usedStreets.put(streetId, street);
            }

        }

        for (CarPath carPath : input.carPaths) {
            Integer integer = carPath.getStreetIds().get(0);
            usedStreets.get(integer).addCar();
        }

        LinkedHashMap<Integer, StreetExt> usedStreetsStorted = usedStreets.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> e.getValue().numberOfCarsAtStart))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new));

        for (Integer integer : streets.keySet()) {
            if(usedStreetsStorted.get(integer) == null) {
                unusedStreets.put(integer, streets.get(integer));
            }
        }

        for (Integer usedStreetId : usedStreetsStorted.keySet()) {
            StreetExt usedStreet = usedStreetsStorted.get(usedStreetId);
            if(usedIntersectionMap.get(usedStreet.getStartIntersection()) == null) {
                usedIntersectionMap.put(usedStreet.getStartIntersection(), new Intersection());
            }
            usedIntersectionMap.get(usedStreet.getStartIntersection()).outgoingStreets.put(usedStreet.getStreetId(), usedStreet);

            if(usedIntersectionMap.get(usedStreet.getEndIntersection()) == null) {
                usedIntersectionMap.put(usedStreet.getEndIntersection(), new Intersection());
            }
            usedIntersectionMap.get(usedStreet.getEndIntersection()).incomingStreets.put(usedStreet.getStreetId(), usedStreet);
        }

        for (Integer unusedStreetId : unusedStreets.keySet()) {
            StreetExt usedStreet = unusedStreets.get(unusedStreetId);
            if(usedIntersectionMap.get(usedStreet.getStartIntersection()) == null) {
                if (unusedIntersectionMap.get(usedStreet.getStartIntersection()) == null) {
                    unusedIntersectionMap.put(usedStreet.getStartIntersection(), new Intersection());
                }
                unusedIntersectionMap.get(usedStreet.getStartIntersection()).outgoingStreets.put(usedStreet.getStreetId(), usedStreet);
            } else {
                if(usedIntersectionMap.get(usedStreet.getStartIntersection()) == null) {
                    usedIntersectionMap.put(usedStreet.getStartIntersection(), new Intersection());
                }
                usedIntersectionMap.get(usedStreet.getStartIntersection()).outgoingStreets.put(usedStreet.getStreetId(), usedStreet);
            }

            if(usedIntersectionMap.get(usedStreet.getEndIntersection()) == null) {
                if (unusedIntersectionMap.get(usedStreet.getEndIntersection()) == null) {
                    unusedIntersectionMap.put(usedStreet.getEndIntersection(), new Intersection());
                }
                unusedIntersectionMap.get(usedStreet.getEndIntersection()).incomingStreets.put(usedStreet.getStreetId(), usedStreet);
            } else {
                if(usedIntersectionMap.get(usedStreet.getEndIntersection()) == null) {
                    usedIntersectionMap.put(usedStreet.getEndIntersection(), new Intersection());
                }
                usedIntersectionMap.get(usedStreet.getEndIntersection()).incomingStreets.put(usedStreet.getStreetId(), usedStreet);
            }
        }

        List<IntersectionSchedule> scheduleList = new ArrayList<>();
        for (Integer key : usedIntersectionMap.keySet()) {
            Intersection intersection = usedIntersectionMap.get(key);
            List<GreenLightDuration> greenLightDurations = new ArrayList<>();
            for (Integer streetId : intersection.incomingStreets.keySet()) {
                greenLightDurations.add(new GreenLightDuration(intersection.incomingStreets.get(streetId).getStreetName(), 1));
            }
            scheduleList.add(new IntersectionSchedule(key, intersection.incomingStreets.size(), greenLightDurations));
        }

        for (Integer key : unusedIntersectionMap.keySet()) {
            Intersection intersection = unusedIntersectionMap.get(key);
            List<GreenLightDuration> greenLightDurations = new ArrayList<>();
            for (Integer streetId : intersection.incomingStreets.keySet()) {
                greenLightDurations.add(new GreenLightDuration(intersection.incomingStreets.get(streetId).getStreetName(), 1));
            }
            scheduleList.add(new IntersectionSchedule(key, intersection.incomingStreets.size(), greenLightDurations));
        }

        return Output.builder().numberOfIntersectionsSchedules(usedIntersectionMap.keySet().size() + unusedIntersectionMap.keySet().size())
                .intersectionSchedules(scheduleList)
                .build();
    }

    public class Intersection {
        public Map<Integer, StreetExt> incomingStreets = new LinkedHashMap<>();
        public Map<Integer, StreetExt> outgoingStreets = new LinkedHashMap<>();
    }

    public class StreetExt extends Street {

        Integer numberOfCarsAtStart = 0;

        public StreetExt(Street street) {
            super(street.streetId, street.streetName, street.startIntersection, street.endIntersection, street.timeToGetFromStartToEnd);
        }

        public void addCar() {
            numberOfCarsAtStart--;
        }
    }
}
