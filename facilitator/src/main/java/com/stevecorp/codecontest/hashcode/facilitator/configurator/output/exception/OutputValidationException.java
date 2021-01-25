package com.stevecorp.codecontest.hashcode.facilitator.configurator.output.exception;

import static java.text.MessageFormat.format;

public class OutputValidationException extends RuntimeException {

    public OutputValidationException(final String reason) {
        super(format("OUTPUT VALIDATION ERROR: {0}", reason));
    }

}
