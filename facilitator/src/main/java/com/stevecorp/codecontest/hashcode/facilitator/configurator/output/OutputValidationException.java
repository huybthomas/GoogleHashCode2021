package com.stevecorp.codecontest.hashcode.facilitator.configurator.output;

import static java.text.MessageFormat.format;

/**
 * The exception that will be thrown on validation exceptions.
 */
public class OutputValidationException extends RuntimeException {

    public OutputValidationException(final String reason) {
        super(format("OUTPUT VALIDATION ERROR: {0}", reason));
    }

}
