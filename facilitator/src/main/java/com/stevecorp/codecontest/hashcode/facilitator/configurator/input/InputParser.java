package com.stevecorp.codecontest.hashcode.facilitator.configurator.input;

import java.util.List;

/**
 * The component that transforms the input file content to a POJO.
 */
public interface InputParser<T extends InputModel> {

    /**
     * The method that transforms the file contents to the input model POJO.
     *
     * @param input the input file content in List<String> format
     * @return an InputModel reference.
     */
    T parseInput(List<String> input);

}
