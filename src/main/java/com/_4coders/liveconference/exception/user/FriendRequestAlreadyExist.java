package com._4coders.liveconference.exception.user;

import com._4coders.liveconference.entities.user.friend.FriendRequest;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicates that a {@link FriendRequest} was found while trying to create another
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/03/2020
 */
@Getter
@Flogger
public class FriendRequestAlreadyExist extends RuntimeException {
    public FriendRequestAlreadyExist(String message) {
        super(message);
        log.atFinest().log("Throwing FriendRequestAlreadyExist with message [%s]", message);
    }
}
