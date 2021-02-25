package com.stevecorp.codecontest.hashcode.hashcode2021.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.ParameterizedAlgorithm;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Input;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Input.Street;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output.GreenLightDuration;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output.IntersectionSchedule;

import java.util.*;

public class Algorithm1 extends ParameterizedAlgorithm<Input, Output> {

    public static final String PARAMETER_1_NAME = "P1";
    public static final String PARAMETER_2_NAME = "P2";

    private long parameter1Value;
    private String parameter2Value;

    public Set<Street> usedStreets = new HashSet<>();
    public Set<Street> unusedStreets = new HashSet<>();
    public Map<Integer, Street> streets = new HashMap<>();
    public Map<Integer, Intersection> usedIntersectionMap = new HashMap<>();
    public Map<Integer, Intersection> unusedIntersectionMap = new HashMap<>();

    @Override
    public void handleParameters(final Map<String, Object> parameters) {
        parameter1Value = (long) parameters.get(PARAMETER_1_NAME);
        parameter2Value = (String) parameters.get(PARAMETER_2_NAME);
    }

    @Override
    public Output solve(final Input input) {
        for (Street street : input.getStreets()) {
            streets.put(street.getStreetId(), street);
        }

        System.out.println("test");
        for (Input.CarPath carPath : input.carPaths) {
            for (Integer streetId : carPath.streetIds) {
                Street street= streets.get(streetId);
                usedStreets.add(street);
            }
        }

        unusedStreets = new HashSet<>(streets.values());
        unusedStreets.removeAll(usedStreets);

        for (Street usedStreet : usedStreets) {
            if(usedIntersectionMap.get(usedStreet.getStartIntersection()) == null) {
                usedIntersectionMap.put(usedStreet.getStartIntersection(), new Intersection());
            }
            usedIntersectionMap.get(usedStreet.getStartIntersection()).outgoingStreets.put(usedStreet.getStreetId(), usedStreet);
            if(usedIntersectionMap.get(usedStreet.getEndIntersection()) == null) {
                usedIntersectionMap.put(usedStreet.getEndIntersection(), new Intersection());
            }
            usedIntersectionMap.get(usedStreet.getEndIntersection()).incomingStreets.put(usedStreet.getStreetId(), usedStreet);
        }

        for (Street usedStreet : unusedStreets) {
            if(usedIntersectionMap.get(usedStreet.getStartIntersection()) == null) {
                if (unusedIntersectionMap.get(usedStreet.getStartIntersection()) == null) {
                    unusedIntersectionMap.put(usedStreet.getStartIntersection(), new Intersection());
                }
                unusedIntersectionMap.get(usedStreet.getStartIntersection()).outgoingStreets.put(usedStreet.getStreetId(), usedStreet);
            }

            if(usedIntersectionMap.get(usedStreet.getEndIntersection()) == null) {
                if (unusedIntersectionMap.get(usedStreet.getEndIntersection()) == null) {
                    unusedIntersectionMap.put(usedStreet.getEndIntersection(), new Intersection());
                }
                unusedIntersectionMap.get(usedStreet.getEndIntersection()).incomingStreets.put(usedStreet.getStreetId(), usedStreet);
            } else {
                if(usedIntersectionMap.get(usedStreet.getStartIntersection()) == null) {
                    usedIntersectionMap.put(usedStreet.getStartIntersection(), new Intersection());
                }
                usedIntersectionMap.get(usedStreet.getStartIntersection()).outgoingStreets.put(usedStreet.getStreetId(), usedStreet);
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
        public Map<Integer, Street> incomingStreets = new HashMap<>();
        public Map<Integer, Street> outgoingStreets = new HashMap<>();
    }
}
