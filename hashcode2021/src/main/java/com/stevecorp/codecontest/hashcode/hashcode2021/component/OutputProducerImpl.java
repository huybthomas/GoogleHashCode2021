package com.stevecorp.codecontest.hashcode.hashcode2021.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputProducer;

import java.util.ArrayList;
import java.util.List;

public class OutputProducerImpl implements OutputProducer<Output> {

    @Override
    public List<String> produceOutput(final Output output) {
        final List<String> outputString = new ArrayList<>();

        outputString.add(String.valueOf(output.numberOfIntersectionsSchedules));

        for (final Output.IntersectionSchedule intersectionSchedule : output.intersectionSchedules) {
            outputString.add(String.valueOf(intersectionSchedule.intersectionId));
            outputString.add(String.valueOf(intersectionSchedule.numberOfIncomingStreets));
            for (final Output.GreenLightDuration greenLightDuration : intersectionSchedule.greenLightDurations) {
                outputString.add(greenLightDuration.streetName + " " + greenLightDuration.greenLightDuration);
            }
        }

        return outputString;
    }

}
