package com.stevecorp.codecontest.hashcode.facilitator.configurator.output;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.exception.OutputValidationException;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

public interface OutputValidator<T extends InputModel, U extends OutputModel> {

    void validateOutput(T input, U output) throws OutputValidationException;

}
