package com._4coders.liveconference.exception.user;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicates that the given response is unknown
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/03/2020
 */
@Getter
@Flogger
public class UnknownResponseValueException extends RuntimeException {
    private String response;

    public UnknownResponseValueException(String message, String response) {
        super(message);
        log.atFinest().log("UnknownResponseValueException was thrown with message [%s] and response [%s]", message, response);
        this.response = response;
    }
}
