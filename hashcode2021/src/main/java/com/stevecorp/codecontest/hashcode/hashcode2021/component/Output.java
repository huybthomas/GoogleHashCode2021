package com.stevecorp.codecontest.hashcode.hashcode2021.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Output implements OutputModel {

    public int numberOfIntersectionsSchedules;
    public List<IntersectionSchedule> intersectionSchedules;

    @Getter
    @Setter
    @Builder
    public static final class IntersectionSchedule {

        public int intersectionId;
        public int numberOfIncomingStreets;
        public List<GreenLightDuration> greenLightDurations;

        public IntersectionSchedule(
                final int intersectionId,
                final int numberOfIncomingStreets,
                final List<GreenLightDuration> greenLightDurations) {
            this.intersectionId = intersectionId;
            this.numberOfIncomingStreets = numberOfIncomingStreets;
            this.greenLightDurations = greenLightDurations;
        }

    }

    @Getter
    @Setter
    @Builder
    public static final class GreenLightDuration {

        public String streetName;
        public int greenLightDuration;

        public GreenLightDuration(final String streetName, final int greenLightDuration) {
            this.streetName = streetName;
            this.greenLightDuration = greenLightDuration;
        }

    }

}
