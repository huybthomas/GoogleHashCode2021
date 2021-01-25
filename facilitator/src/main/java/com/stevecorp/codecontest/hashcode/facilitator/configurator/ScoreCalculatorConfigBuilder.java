package com.stevecorp.codecontest.hashcode.facilitator.configurator;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.model.OutputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.score.ScoreCalculator;

public interface ScoreCalculatorConfigBuilder<T extends InputModel, U extends OutputModel> {

    OutputProducerConfigBuilder<U> withScoreCalculator(ScoreCalculator<T, U> scoreCalculator);

}
