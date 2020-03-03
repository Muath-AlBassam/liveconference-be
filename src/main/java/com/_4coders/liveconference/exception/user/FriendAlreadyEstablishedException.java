package com._4coders.liveconference.exception.user;

import com._4coders.liveconference.entities.user.User;
import com._4coders.liveconference.entities.user.friend.Friend;
import lombok.Getter;
import lombok.extern.flogger.Flogger;


/**
 * Indicate that a {@link User} is trying to add another {@link User} as a {@link Friend} but he is already a {@link Friend}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/03/2020
 */
@Getter
@Flogger
public class FriendAlreadyEstablishedException extends RuntimeException {

    public FriendAlreadyEstablishedException(String message) {
        super(message);
        log.atFinest().log("Throwing FriendAlreadyEstablishedException with message [%s]", message);
    }
}
