package com._4coders.liveconference.exception.common;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that generation of unique UUID has failed
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/02/2020
 */
@Getter
@Flogger
public class UUIDUniquenessException extends RuntimeException {
    private int numberOfTries;

    public UUIDUniquenessException(String message, int numberOfTries) {
        super(message);
        log.atFine().log("UUIDUniquenessException was thrown with message [%s] and number of tries [$d]", message,
                numberOfTries);
        this.numberOfTries = numberOfTries;
    }

    public UUIDUniquenessException(String message) {
        super(message);
        log.atFine().log("UUIDUniquenessException was thrown with message [%s] and no number of tries was given",
                message);

    }
}
