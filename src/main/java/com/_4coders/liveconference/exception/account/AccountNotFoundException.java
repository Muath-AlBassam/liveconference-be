package com._4coders.liveconference.exception.account;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

import java.util.UUID;

/**
 * Indicate that an {@code Account} was not found with the given data.
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/02/2020
 */
@Getter
@Flogger
public class AccountNotFoundException extends RuntimeException {
    private Long accountId;
    private String accountEmail;
    private UUID accountUuid;


    public AccountNotFoundException(String message) {
        super(message);
        log.atFinest().log("AccountNotFoundException was thrown with message [%s]", message);
    }

    public AccountNotFoundException(String message, String accountEmail) {
        super(message);
        log.atFinest().log("AccountNotFoundException was thrown with message [%s] and Account email [%s]", message, accountEmail);
        this.accountEmail = accountEmail;
    }

    public AccountNotFoundException(String message, Long accountId) {
        super(message);
        log.atFinest().log("AccountNotFoundException was thrown with message [%s] and Account id [%d]", message,
                accountId);
        this.accountId = accountId;
    }

    public AccountNotFoundException(String message, UUID accountUuid) {
        super(message);
        log.atFinest().log("AccountNotFoundException was thrown with message [%s] and Account UUID [%s]", message,
                accountUuid);
        this.accountUuid = accountUuid;
    }
}
