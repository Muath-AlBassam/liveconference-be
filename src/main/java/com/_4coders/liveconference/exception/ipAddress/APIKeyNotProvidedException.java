package com._4coders.liveconference.exception.ipAddress;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that no {@code APIKey} was provided or it's invalid(NOT EXPIRED),
 * this is used with the {@code Ip} provider
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 09/02/2020
 */
@Getter
@Flogger
public class APIKeyNotProvidedException extends RuntimeException {
    private String apiKey;

    public APIKeyNotProvidedException(String message, String apiKey) {
        super(message);
        log.atFine().log("APIKeyNotProvidedException was thrown with message [%s] and APIKey [%s]", message, apiKey);
        this.apiKey = apiKey;
    }

    public APIKeyNotProvidedException(String message) {
        super(message);
        log.atFine().log("APIKeyNotProvidedException was thrown with message [%s]", message);
    }
}
