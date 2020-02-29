package com._4coders.liveconference.exception.account;

import com._4coders.liveconference.entities.account.activation.AccountActivation;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that the no {@link AccountActivation} was found
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 28/02/2020
 */
@Getter
@Flogger
public class AccountActivationExpiredException extends RuntimeException {
    private String accountEmail;

    public AccountActivationExpiredException(String message, String accountEmail) {
        super(message);
        log.atFine().log("AccountActivationExpiredException was thrown with message [%s] and email [%s]", message, accountEmail);
        this.accountEmail = accountEmail;
    }

    public AccountActivationExpiredException(String message) {
        super(message);
        log.atFine().log("AccountActivationExpiredException was thrown with message [%s]", message);

    }
}
