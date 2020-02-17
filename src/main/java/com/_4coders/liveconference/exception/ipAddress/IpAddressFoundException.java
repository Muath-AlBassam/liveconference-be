package com._4coders.liveconference.exception.ipAddress;

import com._4coders.liveconference.entities.ipAddress.IpAddress;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that an {@code IpAddress} was found with the same given data, this {@code Exception} should be thrown when
 * an {@code IpAddress} with the given data exist and we're trying to save on it (not updating)
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 09/02/2020
 */
@Getter
@Flogger
public class IpAddressFoundException extends Exception {
    private IpAddress toSave;

    public IpAddressFoundException(String message, IpAddress toSave) {
        super(message);
        log.atFine().log("IpAddressFoundException was thrown with message [%s] and IpAddress to save [%s]", message, toSave);
        this.toSave = toSave;
    }

    public IpAddressFoundException(String message) {
        super(message);
        log.atFine().log("IpAddressFoundException was thrown with message [%s]", message);

    }
}
