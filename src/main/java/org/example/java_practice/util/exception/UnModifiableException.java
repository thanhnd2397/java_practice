package org.example.java_practice.util.exception;

import java.io.Serial;

public class UnModifiableException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -873179930383529923L;

    public UnModifiableException() {
        super();
    }

    public UnModifiableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnModifiableException(String message) {
        super(message);
    }

    public UnModifiableException(Throwable cause) {
        super(cause);
    }
}
