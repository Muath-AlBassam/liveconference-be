package com._4coders.liveconference.exception.ipAddress;

import lombok.Getter;
import lombok.extern.flogger.Flogger;
import org.springframework.http.HttpStatus;

/**
 * Indicate that an unknown {@code response} was received from the {@code Ip} provider,
 * this is used with the {@code Ip} provider
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 09/02/2020
 */
@Getter
@Flogger
public class IpProviderUnknownException extends RuntimeException {
    private HttpStatus httpStatus;
    private String errorResponse;

    public IpProviderUnknownException(String message, HttpStatus httpStatus, String errorResponse) {
        super(message);
        log.atSevere().log("IpProviderUnknownException was thrown with message [%s] " +
                "and HttpStatus [%s] and error response from provider [%s]", httpStatus.toString(), errorResponse);
        this.httpStatus = httpStatus;
        this.errorResponse = errorResponse;
    }

    public IpProviderUnknownException(String message) {
        super(message);
        log.atSevere().log("IpProviderUnknownException was thrown with message [%s]");
    }
}
