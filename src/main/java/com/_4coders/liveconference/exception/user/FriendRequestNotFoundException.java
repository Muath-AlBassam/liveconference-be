package com._4coders.liveconference.exception.user;

import com._4coders.liveconference.entities.user.friend.FriendRequest;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

import java.util.UUID;

/**
 * Indicates that the requested {@link FriendRequest} was not found
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/03/2020
 */
@Getter
@Flogger
public class FriendRequestNotFoundException extends RuntimeException {
    private UUID adder;
    private UUID added;

    public FriendRequestNotFoundException(String message, UUID adder, UUID added) {
        super(message);
        log.atFinest().log("FriendRequestNotFoundException was thrown with message [%s], adder [%s] and added [%s]",
                message, adder, added);
        this.adder = adder;
        this.added = added;
    }
}
