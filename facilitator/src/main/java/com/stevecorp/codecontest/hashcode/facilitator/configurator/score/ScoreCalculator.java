package com.stevecorp.codecontest.hashcode.facilitator.configurator.score;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;

public interface ScoreCalculator<T extends InputModel, U extends OutputModel> {

    long calculateScore(T input, U output);

}
