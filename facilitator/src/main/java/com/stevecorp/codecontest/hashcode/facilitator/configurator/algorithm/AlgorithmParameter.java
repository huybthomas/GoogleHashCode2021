package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm;

import lombok.Getter;

@Getter
public abstract class AlgorithmParameter {

    private final String name;

    public AlgorithmParameter(final String name) {
        this.name = name;
    }

}
