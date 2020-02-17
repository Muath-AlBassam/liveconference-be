package com._4coders.liveconference.exception.user;

import lombok.Getter;
import lombok.extern.flogger.Flogger;

/**
 * Indicate that a {@code User} trying to exceed the maximum number of users allowed
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 17/02/2020
 */
@Getter
@Flogger
public class MaximumNumberOfUserReachedException extends RuntimeException {
    private int numberOfUsers;
    private Long accountId;

    public MaximumNumberOfUserReachedException(String message) {
        super(message);
        log.atFine().log("MaximumNumberOfUserReachedException was thrown with message [%s]", message);
    }

    public MaximumNumberOfUserReachedException(String message, int numberOfUsers) {
        super(message);
        log.atFine().log("MaximumNumberOfUserReachedException was thrown with message [%s] and numberOfUsers [%d]",
                message, numberOfUsers);
        this.numberOfUsers = numberOfUsers;
    }

    public MaximumNumberOfUserReachedException(String message, Long accountId) {
        super(message);
        log.atFine().log("MaximumNumberOfUserReachedException was thrown with message [%s] and accountId [%d]",
                message, accountId);
        this.accountId = accountId;
    }

    public MaximumNumberOfUserReachedException(String message, int numberOfUsers, Long accountId) {
        super(message);
        log.atFine().log("MaximumNumberOfUserReachedException was thrown with message [%s], numberOfUsers [%d] and accountId [%d]",
                message, numberOfUsers, accountId);
        this.numberOfUsers = numberOfUsers;
        this.accountId = accountId;
    }
}
