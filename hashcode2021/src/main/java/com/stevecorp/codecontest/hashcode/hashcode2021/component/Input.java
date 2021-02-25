package com.stevecorp.codecontest.hashcode.hashcode2021.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder(toBuilder = true)
public class Input implements InputModel {

    public int simulationDurationSeconds;
    public int numberOfIntersections;
    public int numberOfStreets;
    public int numberOfCars;
    public int bonusPointsForReachingDestinationInTime;
    public List<Street> streets;
    public List<CarPath> carPaths;

    @Getter
    @Setter
    @Builder(toBuilder = true)
    public static final class Street {

        public int streetId;
        public String streetName;
        public int startIntersection;
        public int endIntersection;
        public int timeToGetFromStartToEnd;

        public Street(final int streetId, final String streetName, final int startIntersection, final int endIntersection, final int timeToGetFromStartToEnd) {
            this.streetId = streetId;
            this.streetName = streetName;
            this.startIntersection = startIntersection;
            this.endIntersection = endIntersection;
            this.timeToGetFromStartToEnd = timeToGetFromStartToEnd;
        }

    }

    @Getter
    @Setter
    @Builder(toBuilder = true)
    public static final class CarPath {

        public int numberOfStreets;
        public List<Integer> streetIds;

        public CarPath(final int numberOfStreets, final List<Integer> streetIds) {
            this.numberOfStreets = numberOfStreets;
            this.streetIds = streetIds;
        }

    }

    @Override
    public Input cloneInput() {
        return this.toBuilder()
                .streets(this.streets.stream()
                        .map(street -> street.toBuilder().build())
                        .collect(Collectors.toList()))
                .carPaths(this.carPaths.stream()
                        .map(carPath -> carPath.toBuilder().build())
                        .collect(Collectors.toList()))
                .build();
    }

}
