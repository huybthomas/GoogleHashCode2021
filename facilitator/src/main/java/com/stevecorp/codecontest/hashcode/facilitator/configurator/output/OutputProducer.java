package com.stevecorp.codecontest.hashcode.facilitator.configurator.output;

import java.util.List;

/**
 * The component that transforms the algorithm output into file output suitable for submission
 */
public interface OutputProducer<T extends OutputModel> {

    /**
     * The method that transforms the algorithm output to a file output suitable for submission.
     *
     * @param output the algorithm output
     * @return the output file content in List<String> format
     */
    List<String> produceOutput(T output);

}
