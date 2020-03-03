package com._4coders.liveconference.exception.account;

import com._4coders.liveconference.entities.account.Account;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * when {@link Account}s try to initiate an action between them and one of them has blocked the other then this
 * exception is ued
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/03/2020
 */
@Getter
@Flogger
public class AccountsBlockedException extends RuntimeException {

    public AccountsBlockedException(String message) {
        super(message);
        log.atFinest().log("Throwing AccountsBlockedException with message [%s]", message);
    }
}
