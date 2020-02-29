package com._4coders.liveconference.exception.account;

import com._4coders.liveconference.entities.account.Account;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that an {@link Account} was found with the same given {@code phoneNumber}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 23/02/2020
 */
@Getter
@Flogger
public class AccountFoundByPhoneNumberException extends AccountFoundException {
    private String foundPhoneNumber;

    public AccountFoundByPhoneNumberException(String message, String foundPhoneNumber) {
        super(message);
        log.atFine().log("AccountFoundByEmailException was thrown with message [%s] and found PhoneNumber", message,
                foundPhoneNumber);
        this.foundPhoneNumber = foundPhoneNumber;
    }
}
