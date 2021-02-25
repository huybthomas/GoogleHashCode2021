package com.stevecorp.codecontest.hashcode.hashcode2021.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.BasicAlgorithm;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Input;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.Output;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StevesBmazingAlgorithm extends BasicAlgorithm<Input, Output> {

    @Override
    public Output solve(final Input input) {

        final List<Input.Street> streets = input.streets;

        final Map<Integer, Map<Integer, Integer>> volumeTracking = new HashMap<>();
        for (final Input.Street street : input.streets) {
            if (!volumeTracking.containsKey(street.endIntersection)) {
                volumeTracking.put(street.endIntersection, new HashMap<>());
            }
            final Map<Integer, Integer> tLightVolume = volumeTracking.get(street.endIntersection);
            if (!tLightVolume.containsKey(street.getStreetId())) {
                tLightVolume.put(street.getStreetId(), 0);
            }
            volumeTracking.put(street.endIntersection, tLightVolume);
        }

        for (final Input.CarPath carPath : input.carPaths) {
            for (final Integer streetId : carPath.streetIds) {
                final Input.Street street = streets.get(streetId);
                final Map<Integer, Integer> tLightVolume = volumeTracking.get(street.endIntersection);
                tLightVolume.put(streetId, tLightVolume.get(streetId) + 1);
                volumeTracking.put(street.endIntersection, tLightVolume);
            }
        }

        final List<Output.IntersectionSchedule> intersectionSchedules = new ArrayList<>();
        for (final Map.Entry<Integer, Map<Integer, Integer>> volumeTrackingEntry : volumeTracking.entrySet()) {
            final int intersectionId = volumeTrackingEntry.getKey();
            final Map<Integer, Integer> tLightVolume = volumeTrackingEntry.getValue();
            final Map<Integer, Integer> tLightVolumeFiltered = tLightVolume.entrySet().stream()
                    .filter(entry -> entry.getValue() != 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (!tLightVolumeFiltered.isEmpty()) {
                final List<Output.GreenLightDuration> greenLightDurations = new ArrayList<>();

                final int[] intermediary = { 0 };
                final List<Tmp> tmpList = tLightVolumeFiltered.entrySet().stream()
                        .map(entry -> new Tmp(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparingInt(o -> o.volume))
                        .peek(tmp -> tmp.index = ++intermediary[0])
                        .collect(Collectors.toList());
                for (final Tmp tmp : tmpList) {
                    greenLightDurations.add(new Output.GreenLightDuration(streets.get(tmp.streetId).streetName, tmp.index));
                }
                intersectionSchedules.add(new Output.IntersectionSchedule(intersectionId, greenLightDurations.size(), greenLightDurations));

                // todo improve order
//                final List<Output.GreenLightDuration> greenLightDurations = new ArrayList<>();
//                final int totalVolume = tLightVolumeFiltered.values().stream().mapToInt(x -> x).sum();
//                for (final Map.Entry<Integer, Integer> tLightVolumeEntry : tLightVolumeFiltered.entrySet()) {
//                    final int streetId = tLightVolumeEntry.getKey();
//                    final int volume = tLightVolumeEntry.getValue();
//                    final double volumePct = 1.0 * volume / totalVolume;
//                    int value = (int) (volumePct * 10);
//                    if (value > input.simulationDurationSeconds) {
//                        value = input.simulationDurationSeconds;
//                    }
//                    if (value == 0) {
//                        value = 1;
//                    }
//                    greenLightDurations.add(new Output.GreenLightDuration(streets.get(streetId).streetName, value));
//                }
//                intersectionSchedules.add(new Output.IntersectionSchedule(intersectionId, greenLightDurations.size(), greenLightDurations));
            }
        }

        return Output.builder()
                .numberOfIntersectionsSchedules(intersectionSchedules.size())
                .intersectionSchedules(intersectionSchedules)
                .build();
    }

    public static final class Tmp {

        public int streetId;
        public int volume;
        public int index;
        public int startingCars;

        public Tmp(final int streetId, final int volume) {
            this.streetId = streetId;
            this.volume = volume;
        }

    }

}
