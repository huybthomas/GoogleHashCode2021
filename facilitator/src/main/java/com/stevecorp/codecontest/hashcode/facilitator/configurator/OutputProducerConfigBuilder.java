package com.stevecorp.codecontest.hashcode.facilitator.configurator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputProducer;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

public interface OutputProducerConfigBuilder {

    FinalConfigBuilder withOutputProducer(OutputProducer<? extends OutputModel> outputProducer);

}
