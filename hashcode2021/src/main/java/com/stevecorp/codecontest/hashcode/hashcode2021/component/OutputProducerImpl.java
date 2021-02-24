package com.stevecorp.codecontest.hashcode.hashcode2021.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputProducer;

import java.util.Collections;
import java.util.List;

public class OutputProducerImpl implements OutputProducer<Output> {

    @Override
    public List<String> produceOutput(final Output output) {
        return Collections.emptyList();
    }

}
