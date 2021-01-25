package com.stevecorp.codecontest.hashcode.facilitator.configurator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputValidator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

public interface OutputValidatorConfigBuilder<T extends InputModel, U extends OutputModel> {

    ScoreCalculatorConfigBuilder<T, U> dontValidateOutput();
    ScoreCalculatorConfigBuilder<T, U> withOutputValidator(OutputValidator<T, U> outputValidator);

}
