package com._4coders.liveconference.exception.ipAddress;


import com._4coders.liveconference.entities.ipAddress.IpAddress;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that an {@code IpAddress} was found to be harmful (Threat being either a known attacker or a known abuser)
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 09/02/2020
 */
@Getter
@Flogger
public class HarmfulIpAddressException extends RuntimeException {
    private IpAddress harmfulIpAddress;

    public HarmfulIpAddressException(String message, IpAddress harmfulIpAddress) {
        super(message);
        log.atFine().log("HarmfulIpAddressException was thrown with message [%s] and harmful IpAddress [%s]", message,
                harmfulIpAddress);
        this.harmfulIpAddress = harmfulIpAddress;
    }

    public HarmfulIpAddressException(String message) {
        super(message);
        log.atFine().log("HarmfulIpAddressException was thrown with message [%s]", message);
    }
}
