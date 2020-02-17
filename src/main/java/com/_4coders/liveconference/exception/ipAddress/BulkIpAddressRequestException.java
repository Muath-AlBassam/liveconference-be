package com._4coders.liveconference.exception.ipAddress;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that the {@code List} of {@code IpAddresses} given to the API exceeded's the maximum amount (now it's 100),
 * this is used with the {@code Ip} provider
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 09/02/2020
 */
@Getter
@Flogger
public class BulkIpAddressRequestException extends Exception {
    private int numberOfIpAddresses;

    public BulkIpAddressRequestException(String message, int numberOfIpAddresses) {
        super(message);
        log.atFine().log("BulkIpAddressRequestException was thrown with message [%s] and number of IpAddresses [%d]",
                message, numberOfIpAddresses);
        this.numberOfIpAddresses = numberOfIpAddresses;
    }

    public BulkIpAddressRequestException(String message) {
        super(message);
        log.atFine().log("BulkIpAddressRequestException was thrown with message [%s]", message);
    }
}
