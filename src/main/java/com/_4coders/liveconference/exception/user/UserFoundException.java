package com._4coders.liveconference.exception.user;

import com._4coders.liveconference.entities.user.User;
import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that a {@code User} was found with the same given {@code UserName}
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 17/02/2020
 */
@Getter
@Flogger
public class UserFoundException extends RuntimeException {
    private User foundUser;
    private String userName;


    public UserFoundException(String message) {
        super(message);
        log.atFine().log("UserFoundException was thrown with message [%s] ", message);
    }

    public UserFoundException(String message, User foundUser) {
        super(message);
        log.atFine().log("UserFoundException was thrown with message [%s] with found User [%s] ", message, foundUser);
        this.foundUser = foundUser;
    }

    public UserFoundException(String message, String userName) {
        super(message);
        log.atFine().log("UserFoundException was thrown with message [%s] with UserName [%s]", message, userName);
        this.userName = userName;
    }
}
