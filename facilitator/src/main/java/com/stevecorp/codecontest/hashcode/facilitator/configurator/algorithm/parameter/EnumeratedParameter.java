package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmParameter;
import com.stevecorp.codecontest.hashcode.facilitator.util.CollectionUtils;
import lombok.Getter;

import java.util.List;

/**
 * Parameter of type: Enumerated
 *
 * This type of parameter is defined by a specified amount of parameters.
 */
@Getter
public class EnumeratedParameter extends AlgorithmParameter {

    private final List<Object> values;

    public EnumeratedParameter(final String name, final Object value1, final Object... additionalValues) {
        super(name);
        this.values = CollectionUtils.join(value1, additionalValues);
    }

    @Override
    public long getNumberOfScenarios() {
        return values.size();
    }

}
