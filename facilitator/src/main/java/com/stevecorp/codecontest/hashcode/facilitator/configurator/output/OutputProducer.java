package com.stevecorp.codecontest.hashcode.facilitator.configurator.output;

import java.util.List;

public interface OutputProducer<T extends OutputModel> {

    List<String> produceOutput(T output);

}
