package com.stevecorp.codecontest.hashcode.facilitator.configurator.input;

import java.util.List;

public interface InputParser<T extends InputModel> {

    /**
     * The method that transforms the file contents to the input model POJO.
     *
     * @param input the file contents in the List<String> format
     * @return an InputModel reference.
     */
    T parseInput(List<String> input);

}
