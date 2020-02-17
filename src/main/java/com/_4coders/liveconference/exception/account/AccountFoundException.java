package com._4coders.liveconference.exception.account;

import com._4coders.liveconference.entities.account.Account;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that an {@code Account} was found with the same given data, this {@code Exception} should be thrown when
 * an {@code Account} with the given data exist when it shouldn't
 * </br>(e.x: in registration an account was found with the same email as the given one)
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/02/2020
 */
@Getter
@Flogger
public class AccountFoundException extends RuntimeException {
    private Account accountFound;


    public AccountFoundException(String message, Account accountFound) {
        super(message);
        log.atFine().log("AccountFoundException was thrown with message [%s] and Account data [%s]", message,
                accountFound);
        this.accountFound = accountFound;
    }

    public AccountFoundException(String message) {
        super(message);
        log.atFine().log("AccountFoundException was thrown with message [%s] and no Account was given", message);
    }
}
