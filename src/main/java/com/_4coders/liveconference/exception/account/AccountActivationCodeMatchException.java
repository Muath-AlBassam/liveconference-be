package com._4coders.liveconference.exception.account;

import com._4coders.liveconference.entities.account.activation.AccountActivation;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that the the given {@link AccountActivation} {@code Code} doesn't match the fetched one
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 28/02/2020
 */
@Getter
@Flogger
public class AccountActivationCodeMatchException extends RuntimeException {
    private String givenCode;
    private String fetchedCode;

    public AccountActivationCodeMatchException(String message, String givenCode, String fetchedCode) {
        super(message);
        log.atFine().log("AccountActivationExpiredException was thrown with message [%s], givenCode [%s] and " +
                "fetchedCode [%s]", message, givenCode, fetchedCode);
        this.givenCode = givenCode;
        this.fetchedCode = fetchedCode;
    }

    public AccountActivationCodeMatchException(String message) {
        super(message);
        log.atFine().log("AccountActivationExpiredException was thrown with message [%s]", message);
    }
}
