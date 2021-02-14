package com.stevecorp.codecontest.hashcode.facilitator.configurator.input;

/**
 * The input model for the problem solution.
 *
 * This enables you to work with properly named variables.
 *
 * Since we want all algorithm runs to be idempotent, you need to provide functionality to create a clone of the input.
 */
public interface InputModel {

    <T extends InputModel> T cloneInput();

}
