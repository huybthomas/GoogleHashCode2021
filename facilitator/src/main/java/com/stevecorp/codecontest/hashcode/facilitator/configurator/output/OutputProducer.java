package com.stevecorp.codecontest.hashcode.facilitator.configurator.output;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

import java.util.List;

public interface OutputProducer<T extends OutputModel> {

    List<String> produceOutput(T output);

}
