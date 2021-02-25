package com.stevecorp.codecontest.hashcode.hashcode2021;

import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.AlgorithmSpecification;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter.BoundedParameter;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.parameter.EnumeratedParameter;
import com.stevecorp.codecontest.hashcode.hashcode2021.algorithm.Algorithm1;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.InputParserImpl;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.OutputProducerImpl;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.OutputValidatorImpl;
import com.stevecorp.codecontest.hashcode.hashcode2021.component.ScoreCalculatorImpl;

import static com.stevecorp.codecontest.hashcode.hashcode2021.algorithm.Algorithm1.PARAMETER_1_NAME;
import static com.stevecorp.codecontest.hashcode.hashcode2021.algorithm.Algorithm1.PARAMETER_2_NAME;

public class HashCode2021 {

    public static void main(final String... args) {
        HashCodeFacilitator.configurator()
                .forAllInputFiles()
                .withInputParser(InputParserImpl.class)
                .withAlgorithms(
                        AlgorithmSpecification.builder()
                                .parameterizedAlgorithm(Algorithm1.class)
                                .withParameters(
                                        BoundedParameter.of(PARAMETER_1_NAME, 1,1, 1)
                                )
                                .build()
                )
                .withOutputValidator(OutputValidatorImpl.class)
                .withScoreCalculator(ScoreCalculatorImpl.class)
                .withOutputProducer(OutputProducerImpl.class)
                .run();
    }

}
