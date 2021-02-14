package com.stevecorp.codecontest.hashcode.facilitator.configurator.output;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;

/**
 * The optional component that validates the algorithm output.
 */
public interface OutputValidator<T extends InputModel, U extends OutputModel> {

    /**
     * The method that will handle the algorithm output validation.
     *
     * When invalid output is detected, throw an #OutputValidationException
     *
     * @param input the algorithm input
     * @param output the algorithm output
     * @throws OutputValidationException when the algorithm output is invalid, this exception will be thrown
     */
    void validateOutput(T input, U output) throws OutputValidationException;

}
