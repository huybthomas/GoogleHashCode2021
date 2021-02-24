package com.stevecorp.codecontest.hashcode.hashcode2021.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputParser;

import java.util.List;

public class InputParserImpl implements InputParser<Input> {

    @Override
    public Input parseInput(final List<String> input) {
        return Input.builder()
                .build();
    }

}
