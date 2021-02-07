package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmParameter;
import lombok.Getter;

import static java.lang.Math.floorDiv;

@Getter
public class BoundedParameter extends AlgorithmParameter {

    private final long lowerLimit;
    private final long upperLimit;
    private final long stepSize;

    public BoundedParameter(final String name, final long lowerLimit, final long upperLimit, final long stepSize) {
        super(name);
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.stepSize = stepSize;
    }

    @Override
    public long getNumberOfScenarios() {
        return floorDiv(upperLimit - lowerLimit, stepSize) + 1;
    }

}
