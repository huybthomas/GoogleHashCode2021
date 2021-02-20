package com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.AlgorithmType;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.BasicAlgorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.ParameterizedAlgorithm;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import com.stevecorp.codecontest.hashcode.facilitator.configurator.output.OutputModel;
import lombok.Getter;

import java.util.List;

import static com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.AlgorithmType.DEFAULT;
import static com.stevecorp.codecontest.hashcode.facilitator.configurator.algorithm.algorithm.AlgorithmType.PARAMETERIZED;
import static com.stevecorp.codecontest.hashcode.facilitator.util.CollectionUtils.join;

@Getter
@SuppressWarnings({ "unused", "rawtypes", "FieldCanBeLocal" })
public class AlgorithmSpecification<T extends InputModel, U extends OutputModel> {

    private final Class<? extends Algorithm> algorithmClass;
    private final AlgorithmType algorithmType;
    private final List<AlgorithmParameter> parameters;

    private AlgorithmSpecification(final SpecificationBuilder<T, U> builder) {
        this.algorithmClass = builder.algorithmClass;
        this.algorithmType = builder.algorithmType;
        this.parameters = builder.parameters;
    }

    public static Specification_Algorithm<?, ?> builder() {
        return new SpecificationBuilder<>().builder();
    }

    /**************************************************************************************************************
     ***   Definition of Builder (chaining) classes to enforce a method order                                   ***
     **************************************************************************************************************/

    public static final class SpecificationBuilder<T extends InputModel, U extends OutputModel> {

        Class<? extends Algorithm> algorithmClass;
        AlgorithmType algorithmType;
        List<AlgorithmParameter> parameters;

        private SpecificationBuilder() {}

        public Specification_Algorithm<T, U> builder() {
            return new Specification_Algorithm<>(this);
        }

    }

    public static final class Specification_Algorithm<T extends InputModel, U extends OutputModel> {

        final SpecificationBuilder<T, U> builder;

        public Specification_Algorithm(final SpecificationBuilder<T, U> builder) {
            this.builder = builder;
        }

        /**
         * Define a basic algorithm.
         *
         * An algorithm that receives the input and creates the output - once per input file.
         *
         * @param algorithmClass class reference of the BasicAlgorithm implementation
         */
        public Specification_BasicAlgorithm_Final<T, U> basicAlgorithm(Class<? extends BasicAlgorithm> algorithmClass) {
            builder.algorithmClass = algorithmClass;
            builder.algorithmType = DEFAULT;
            return new Specification_BasicAlgorithm_Final<>(this);
        }

        /**
         * Define a parameterized algorithm.
         *
         * An algorithm that receives the input and creates the output - for every possible combination of provided
         *  parameter ranges for every input file.
         *
         * @param algorithmClass class reference of the ParameterizedAlgorithm implementation
         */
        public Specification_ParameterizedAlgorithm_Parameter<T, U> parameterizedAlgorithm(final Class<? extends ParameterizedAlgorithm> algorithmClass) {
            builder.algorithmClass = algorithmClass;
            builder.algorithmType = PARAMETERIZED;
            return new Specification_ParameterizedAlgorithm_Parameter<>(this);
        }

    }

    public static final class Specification_BasicAlgorithm_Final<T extends InputModel, U extends OutputModel> {

        final SpecificationBuilder<T, U> builder;

        public Specification_BasicAlgorithm_Final(final Specification_Algorithm<T, U> specification) {
            this.builder = specification.builder;
        }

        public AlgorithmSpecification<T, U> build() {
            return new AlgorithmSpecification<>(builder);
        }

    }

    public static final class Specification_ParameterizedAlgorithm_Parameter<T extends InputModel, U extends OutputModel> {

        final SpecificationBuilder<T, U> builder;

        public Specification_ParameterizedAlgorithm_Parameter(final Specification_Algorithm<T, U> specification) {
            this.builder = specification.builder;
        }

        /**
         * Defines parameters for the parameterized algorithm.
         *
         * These parameters are instances of classes extending the AlgorithmParameter class. Options are:
         *  - BoundedParameter: a parameter defined by a lower and upper limit, and a step size
         *  - EnumeratedParameter: a parameter defined by distinct values.
         *
         * @param parameter the parameterized algorithm parameter
         * @param additionalParameters additional parameterized algorithm parameters
         */
        public Specification_ParameterizedAlgorithm_Final<T, U> withParameters(final AlgorithmParameter parameter, final AlgorithmParameter... additionalParameters) {
            builder.parameters = join(parameter, additionalParameters);
            return new Specification_ParameterizedAlgorithm_Final<>(this);
        }

    }

    public static final class Specification_ParameterizedAlgorithm_Final<T extends InputModel, U extends OutputModel> {

        final SpecificationBuilder<T, U> builder;

        public Specification_ParameterizedAlgorithm_Final(final Specification_ParameterizedAlgorithm_Parameter<T, U> specification) {
            this.builder = specification.builder;
        }

        public AlgorithmSpecification<T, U> build() {
            return new AlgorithmSpecification<>(builder);
        }

    }

}
