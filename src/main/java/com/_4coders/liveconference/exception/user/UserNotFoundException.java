package com._4coders.liveconference.exception.user;


import lombok.Getter;
import lombok.extern.flogger.Flogger;

import java.util.UUID;

/**
 * Indicate that a {@code User} was not found with the given data.
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 17/02/2020
 */
@Getter
@Flogger
public class UserNotFoundException extends RuntimeException {
    private Long userId;
    private UUID userUuid;
    private String userName;

    public UserNotFoundException(String message) {
        super(message);
        log.atFine().log("UserNotFoundException was thrown with message [%s]", message);
    }

    public UserNotFoundException(String message, Long userId) {
        super(message);
        log.atFine().log("UserNotFoundException was thrown with message [%s] and UserId [%d]", message, userId);
        this.userId = userId;
    }

    public UserNotFoundException(String message, String userName) {
        super(message);
        log.atFine().log("UserNotFoundException was thrown with message [%s] and UserName [%s]", message, userName);
        this.userName = userName;
    }

    public UserNotFoundException(String message, UUID userUuid) {
        super(message);
        log.atFine().log("UserNotFoundException was thrown with message [%s] and UserUuid [%s]", message, userUuid);
        this.userUuid = userUuid;
    }


}
