package com._4coders.liveconference.exception.account;

import com._4coders.liveconference.entities.account.Account;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that we're trying to activate an already activated {@link Account}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 28/02/2020
 */
@Getter
@Flogger
public class AccountAlreadyActivatedException extends RuntimeException {

    public AccountAlreadyActivatedException(String message) {
        super(message);
        log.atFine().log("AccountAlreadyActivatedException was thrown with message [%s]", message);

    }
}
