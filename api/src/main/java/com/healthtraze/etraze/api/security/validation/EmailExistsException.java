package com.healthtraze.etraze.api.security.validation;

@SuppressWarnings("serial")
public class EmailExistsException extends Exception {

    public EmailExistsException(final String message) {
        super(message);
    }

}
