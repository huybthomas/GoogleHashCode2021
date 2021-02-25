package com.stevecorp.codecontest.hashcode.hashcode2021.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputValidationException;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputValidator;

import java.util.HashSet;
import java.util.Set;

public class OutputValidatorImpl implements OutputValidator<Input, Output> {

    @Override
    public void validateOutput(final Input input, final Output output) throws OutputValidationException {

        final Set<Integer> intersectionIds = new HashSet<>();
        for (final Output.IntersectionSchedule intersectionSchedule : output.intersectionSchedules) {

            if (intersectionIds.contains(intersectionSchedule.intersectionId)) {
                throw new OutputValidationException("duplicate intersection in output!");
            }
            intersectionIds.add(intersectionSchedule.intersectionId);

            final Set<String> streetNames = new HashSet<>();
            for (final Output.GreenLightDuration greenLightDuration : intersectionSchedule.greenLightDurations) {
                if (streetNames.contains(greenLightDuration.streetName)) {
                    throw new OutputValidationException("duplicate streetName in intersection schedule!");
                }
                streetNames.add(greenLightDuration.streetName);
            }

        }

    }

}
