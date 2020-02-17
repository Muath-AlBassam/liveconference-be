package com._4coders.liveconference.exception.ipAddress;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that the current {@code APIKey} in use has exceeded it's monthly quota,
 * this is used with the {@code Ip} provider
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 09/02/2020
 */
@Getter
@Flogger
public class APIKeyQuotaExceededException extends RuntimeException {
    private String apiKey;

    public APIKeyQuotaExceededException(String message, String apiKey) {
        super(message);
        log.atSevere().log("APIKeyQuotaExceededException was thrown with message [%s] and APIKey [%s]", message, apiKey);
        this.apiKey = apiKey;
    }

    public APIKeyQuotaExceededException(String message) {
        super(message);
        log.atSevere().log("APIKeyQuotaExceededException was thrown with message [%s]", message);
    }
}
