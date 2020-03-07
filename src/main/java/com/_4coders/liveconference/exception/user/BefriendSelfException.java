package com._4coders.liveconference.exception.user;


import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.user.User;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

import java.util.UUID;

/**
 * Indicates that the the request for friendship is rejects as the {@link User} is trying to befriend himself (or
 * other user from the same {@link Account})
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 06/03/2020
 */
@Getter
@Flogger
public class BefriendSelfException extends RuntimeException {
    private UUID selfUUID;

    public BefriendSelfException(String message, UUID selfUUID) {
        super(message);
        log.atFine().log("BefriendSelfException was thrown with message [%s] and selfUUID [%s]", message, selfUUID);
        this.selfUUID = selfUUID;
    }

    public BefriendSelfException(String message) {
        super(message);
        log.atFine().log("BefriendSelfException was thrown with message [%s]", message);
    }
}
