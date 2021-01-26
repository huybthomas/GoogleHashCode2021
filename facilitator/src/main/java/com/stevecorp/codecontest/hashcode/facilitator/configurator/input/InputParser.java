package com.stevecorp.codecontest.hashcode.facilitator.configurator.input;

import java.util.List;

public interface InputParser<T extends InputModel> {

    T parseInput(List<String> input);

}
