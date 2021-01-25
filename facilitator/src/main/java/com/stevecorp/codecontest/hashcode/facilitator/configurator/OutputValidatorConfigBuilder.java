package com.stevecorp.codecontest.hashcode.facilitator.configurator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputValidator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

public interface OutputValidatorConfigBuilder {

    ScoreCalculatorConfigBuilder dontValidateOutput();
    ScoreCalculatorConfigBuilder withOutputValidator(OutputValidator<? extends InputModel, ? extends OutputModel> outputValidator);

}
