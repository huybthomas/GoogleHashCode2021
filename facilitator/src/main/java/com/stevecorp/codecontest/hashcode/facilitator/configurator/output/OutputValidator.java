package com.stevecorp.codecontest.hashcode.facilitator.configurator.output;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;

public interface OutputValidator<T extends InputModel, U extends OutputModel> {

    void validateOutput(T input, U output) throws OutputValidationException;

}
