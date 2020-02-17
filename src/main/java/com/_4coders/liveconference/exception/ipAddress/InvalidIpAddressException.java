package com._4coders.liveconference.exception.ipAddress;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that a given {@code IpAddress} is invalid or private,this is used with the {@code Ip} provider
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 09/02/2020
 */
@Getter
@Flogger
public class InvalidIpAddressException extends RuntimeException {
    private String ipAddress;

    public InvalidIpAddressException(String message, String ipAddress) {
        super(message);
        log.atFine().log("InvalidIpAddressException was thrown with message [%s] and IpAddress [%s]", message,
                ipAddress);
        this.ipAddress = ipAddress;
    }

    public InvalidIpAddressException(String message) {
        super(message);
        log.atFine().log("InvalidIpAddressException was thrown with message [%s]", message);
    }
}
