package com._4coders.liveconference.exception.account;

import com._4coders.liveconference.entities.account.Account;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that an {@link Account} was found with the same given data, this {@link Exception} should be thrown when
 * an {@link Account} with the given data exist when it shouldn't
 * <br/>(e.x: in registration an account was found with the same email as the given one)
 * <br/>
 * This {@link Exception} is general for more specific cases see: {@link AccountFoundByEmailException} and
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/02/2020
 */
@Getter
@Flogger
public class AccountFoundException extends RuntimeException {

    public AccountFoundException(String message) {
        super(message);
        log.atFine().log("AccountFoundException was thrown with message [%s]", message);
    }
}
