package com._4coders.liveconference.entities.user;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.account.AccountDetails;
import com._4coders.liveconference.entities.global.Page;
import com._4coders.liveconference.exception.account.AccountNotFoundException;
import com._4coders.liveconference.exception.common.UUIDUniquenessException;
import com._4coders.liveconference.exception.sort.MappingSortPropertiesToSchemaPropertiesException;
import com._4coders.liveconference.exception.user.MaximumNumberOfUserReachedException;
import com._4coders.liveconference.exception.user.UserFoundException;
import com._4coders.liveconference.exception.user.UserNotFoundException;
import com._4coders.liveconference.validator.UUIDConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@RestController
@Flogger
@Validated
@RequestMapping("/flogger/users")
@CrossOrigin(origins = "*", exposedHeaders = "X-Auth-Token", allowedHeaders = "*", methods =
        {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST})
public class UserController {
    @Autowired
    private UserService userService;



    /**
     * Creates new {@code User} for the currently logged in {@code Account}
     *
     * @param userName the name of the {@code User} to create
     * @return HTTP OK(200) if the {@code User} was created <br/>
     * HTTP BAD_REQUEST (400) if a {@code User} exists with the same give {@code UserName} or the
     * MAXIMUM_NUMBER_OF_USERS_ALLOWED has been reached<br/>
     * HTTP FORBIDDEN (403) if an error occurred because of the given {@code Account} <br/>
     * HTTP INTERNAL_SERVER_ERROR (500) if no unique {@code UUID} was found
     */
    @PostMapping(params = "userName")
    @JsonView(UserViews.OwnerDetails.class)
    @Transactional
    public ResponseEntity<User> createUserForCurrentlyLoggedInAccount(@AuthenticationPrincipal AccountDetails accountDetails, @RequestParam("userName") @NotBlank String userName) {
        log.atFinest().log("Request for User Creation was Received with UserName [%s], Account Email[%s] and UUID " +
                "[%s]", userName, accountDetails.getAccount().getEmail(), accountDetails.getAccount().getUuid());
        try {
            User registeredUser = userService.createUserByAccount(accountDetails.getAccount(), userName);
            return ResponseEntity.ok(registeredUser);
        } catch (AccountNotFoundException ex) {
            log.atSevere().log("AccountNotFoundException was catched in createUserForCurrentlyLoggedInAccount where " +
                    "it shouldn't be thrown, Account info [%s]", accountDetails.getAccount());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (UserFoundException | MaximumNumberOfUserReachedException ex) {
            log.atFine().log("Returning HTTP BAD_REQUEST (400) in createUserForCurrentlyLoggedInAccount as " +
                    "UserFoundException or MaximumNumberOfUserReachedException was catched");
            return ResponseEntity.badRequest().body(null);
        } catch (UUIDUniquenessException ex) {
            log.atSevere().log("UUIDUniquenessException was catched in createUserForCurrentlyLoggedInAccount as no " +
                    "unique UUID was found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Gets the non deleted {@code Set of User} for the currently logged in {@code Account}
     *
     * @return HTTP OK(200) with the data<br/>
     * HTTP BAD_REQUEST(400) if the given {@code Sort} properties are unknown
     */
    @GetMapping
    @JsonView({UserViews.OwnerDetails.class})
    public ResponseEntity<Set<User>> getNonDeletedAccountUsersForCurrentlyLoggedInAccount(@AuthenticationPrincipal AccountDetails accountDetails, Sort sort) {
        log.atFiner().log("Request for getting Set of Users for currently logged in Account with email [%s] UUID [%s]" +
                "and Sort [%s]", accountDetails.getAccount().getEmail(), accountDetails.getAccount().getUuid(), sort);
        try {
            return ResponseEntity.ok(userService.getNonDeletedUsersByAccountId(accountDetails.getAccount().getId(), sort));
        } catch (MappingSortPropertiesToSchemaPropertiesException ex) {
            log.atFinest().log("Given Sort properties [%s] are unknown", sort);
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Gets the non deleted {@code Set of User} for an {@code Account} by the given {@code email}
     *
     * @param email the {@code email} fpr the {@code Account}
     * @return HTTP OK(200) with the data<br/>
     * HTTP BAD_REQUEST(400) if the given {@code Sort} properties are unknown or no {@code Account} was found the given
     * {@code Email}
     */
    @GetMapping(value = "/account", params = "email")
    public ResponseEntity<Set<User>> getNonDeletedAccountUsersByEmail(@RequestParam(value = "email") @Email String email, Sort sort) {
        log.atFiner().log("Request for getting Set of Users for Account with email [%s] and Sort [%s]", email, sort);
        try {
            return ResponseEntity.ok(userService.getNonDeletedUsersByEmail(email, sort));
        } catch (MappingSortPropertiesToSchemaPropertiesException | AccountNotFoundException ex) {
            log.atFinest().log("Given Sort properties [%s] are unknown or no Account exist with the given email [%s]",
                    sort, email);
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Gets the non deleted {@code Set of User} for an {@code Account} by the given {@code uuid}
     *
     * @param uuid the {@code uuid} fpr the {@code Account}
     * @return HTTP OK(200) with the data<br/>
     * HTTP BAD_REQUEST(400) if the given {@code Sort} properties are unknown or no {@code Account} was found the given
     * {@code Email}
     */
    @GetMapping(value = "/account", params = "uuid")
    public ResponseEntity<Set<User>> getNonDeletedAccountUsersByUuid(@RequestParam("uuid") @UUIDConstraint UUID uuid, Sort sort) {
        log.atFiner().log("Request for getting Set of Users for Account with UUID [%s] and Sort [%s]", uuid, sort);
        try {
            return ResponseEntity.ok(userService.getNonDeletedUsersByUuid(uuid, sort));
        } catch (MappingSortPropertiesToSchemaPropertiesException | AccountNotFoundException ex) {
            log.atFinest().log("Given Sort properties [%s] are unknown or no Account exist with the given UUID [%s]",
                    sort, uuid);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Gets the {@link User} with {@code userName} and not deleted for the currently logged in {@link Account}
     *
     * @param userName the name to search for
     * @return HTTP OK(200) if the {@link User} was found <br/>
     * HTTP BAD_REQUEST(400) if no {@link User} was found
     */
    @GetMapping(params = "userName")
    @JsonView(UserViews.OwnerDetails.class)
    public ResponseEntity<User> getNonDeletedUserByUserNameForCurrentlyLoggedInAccount(@AuthenticationPrincipal AccountDetails accountDetails, @NotBlank @RequestParam("userName") String userName) {
        log.atFinest().log("Request for retrieving a User with name [%s] for the Account with Email [%s] and UUID " +
                        "[%s] which is owned by it", userName, accountDetails.getAccount().getEmail(),
                accountDetails.getAccount().getUuid());

        try {
            User fetchedUser = userService.getNonDeletedUserByUserNameAndAccount(accountDetails.getAccount(), userName);
            return ResponseEntity.ok(fetchedUser);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(value = "/p_users", params = "userName")
    public ResponseEntity<Page<User>> getUsersByUserName(@AuthenticationPrincipal AccountDetails accountDetails,
                                                         @RequestParam("userName") String userName, Pageable pageable) {
        if (accountDetails.getAccount().getCurrentInUseUser() == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            log.atFinest().log("Request for retrieving a Page<User> with name [%s] for the User with name [%s]",
                    userName, accountDetails.getAccount().getCurrentInUseUser().getUserName());
            try {
                Page<User> fetchedUsers = userService.getUsersByUserName(accountDetails.getAccount(), userName, pageable);
                return ResponseEntity.ok(fetchedUsers);
            } catch (MappingSortPropertiesToSchemaPropertiesException ex) {
                return ResponseEntity.badRequest().body(null);
            }
        }
    }


    /**
     * Returns a {@link User} by it's given {@code userName}
     *
     * @param userName the name of the {@link User}
     * @return HTTP OK(200) if the {@link User} is found<br/>
     * HTTP BAD_REQUEST(400) otherwise
     */
    @GetMapping(value = "/user", params = "userName")
    @Transactional
    public ResponseEntity<User> getUserByUserName(@AuthenticationPrincipal AccountDetails accountDetails, @RequestParam("userName") @NotBlank String userName) {
        if (accountDetails.getAccount().getCurrentInUseUser() == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            log.atFinest().log("Request for retrieving a User with name [%s] for the User with name [%s]", userName,
                    accountDetails.getAccount().getCurrentInUseUser().getUserName());
            try {
                User fetchedUser = userService.getUserByUserName(accountDetails.getAccount(),
                        userName);
                return ResponseEntity.ok(fetchedUser);
            } catch (UserNotFoundException ex) {
                return ResponseEntity.badRequest().body(null);
            }
        }
    }

    //THINK may write or modify this one to make the user returned are based on the current logged in user ie you
    // get the user based on your status with them for example User A is Friend with B but not C thus if b searched
    // for A he gets that he is a friend with him.

    /**
     * Returns a {@link User} by it's given {@link UUID}
     *
     * @param uuid the {@link UUID} of the {@link User}
     * @return HTTP OK(200) if the {@link User} is found<br/>
     * HTTP BAD_REQUEST(400) otherwise
     */
    @GetMapping(value = "/user", params = "uuid")
    public ResponseEntity<User> getUserByUUID(@AuthenticationPrincipal AccountDetails accountDetails, @RequestParam("uuid") @UUIDConstraint UUID uuid) {
        if (accountDetails.getAccount().getCurrentInUseUser() == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            log.atFinest().log("Request for retrieving a User with UUID [%s]", uuid);

            try {
                User fetchedUser = userService.getUserByUUID(accountDetails.getAccount(), uuid);
                return ResponseEntity.ok(fetchedUser);
            } catch (UserNotFoundException ex) {
                return ResponseEntity.badRequest().body(null);
            }
        }
    }

    /**
     * Deletes a {@code User} with the given {@code UserName} for the currently logged in {@code Account}
     *
     * @param userName the name of the {@code User} to delete
     * @return HTTP OK(200) if the {@code User} was deleted <br/>
     * HTTP BAD_REQUEST (400) if no {@code User} exists with the same give {@code UserName}<br/>
     * HTTP FORBIDDEN (403) if an error occurred because of the given {@code Account} <br/>
     */
    @DeleteMapping(params = "userName")
    @Transactional
    public ResponseEntity<Boolean> deleteUserByUserNameForCurrentlyLoggedInAccount(@AuthenticationPrincipal AccountDetails accountDetails, @RequestParam("userName") @NotBlank String userName) {
        log.atFinest().log("Request for User Deletion was Received with UserName [%s], Account Email[%s] and UUID" +
                "[%s]", userName, accountDetails.getAccount().getEmail(), accountDetails.getAccount().getUuid());
        try {
            boolean resultOfDeletion =
                    userService.deleteUserByUserNameAndAccountId(accountDetails.getAccount().getId(), userName);
            return ResponseEntity.ok(resultOfDeletion);
        } catch (AccountNotFoundException ex) {
            log.atSevere().log("AccountNotFoundException was catched in deleteUserByUserNameForCurrentlyLoggedInAccount where " +
                    "it shouldn't be thrown, Account info [%s]", accountDetails.getAccount());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (UserNotFoundException ex) {
            log.atFine().log("UserNotFoundException was catched in createUserForCurrentlyLoggedInAccount, returning " +
                    "HTTP BAD_REQUEST(400)");
            return ResponseEntity.badRequest().body(null);
        }
    }

    //only for support user, Secure
    @DeleteMapping(value = "/support", params = "userName")
    @Transactional
    public ResponseEntity<Boolean> deleteUserByUserName(@AuthenticationPrincipal AccountDetails accountDetail, @RequestParam("userName") @NotBlank String userName) {
        log.atFinest().log("Request for User Deletion by UserName was Received with UserName [%s] from Account with " +
                "Email [%s] and UUID [%s]", userName, accountDetail.getAccount().getEmail(), accountDetail.getAccount().getUuid());
        try {
            boolean resultOfDeletion =
                    userService.deleteUserByUserName(userName);
            return ResponseEntity.ok(resultOfDeletion);
        } catch (UserNotFoundException ex) {
            log.atFine().log("UserNotFoundException was catched in deleteUserByUserName, returning " +
                    "HTTP BAD_REQUEST(400)");
            return ResponseEntity.badRequest().body(null);
        }
    }

    //only for support user, Secure
    @DeleteMapping(value = "/support", params = "uuid")
    @Transactional
    public ResponseEntity<Boolean> deleteUserByUserUuid(@AuthenticationPrincipal AccountDetails accountDetail,
                                                        @RequestParam("uuid") @UUIDConstraint UUID uuid) {
        log.atFinest().log("Request for User Deletion by UUID was Received with UUID [%s] from Account with " +
                "Email [%s] and UUID [%s]", uuid, accountDetail.getAccount().getEmail(), accountDetail.getAccount().getUuid());
        try {
            boolean resultOfDeletion =
                    userService.deleteUserByUserUuid(uuid);
            return ResponseEntity.ok(resultOfDeletion);
        } catch (UserNotFoundException ex) {
            log.atFine().log("UserNotFoundException was catched in deleteUserByUserUuid, returning " +
                    "HTTP BAD_REQUEST(400)");
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping(params = {"userName", "uuid"})
    @JsonView(UserViews.OwnerDetails.class)
    @Transactional
    public ResponseEntity<User> updateUserNameForCurrentlyLoggedInAccountByUserUuid(@AuthenticationPrincipal AccountDetails accountDetails, @RequestParam("userName") @NotBlank String userName, @RequestParam("uuid") @UUIDConstraint UUID userUuid) {
        log.atFinest().log("Request for UserName [%s] updating with User UUID [%s] for currently logged in Account " +
                "with Email [%s] and UUID [%s]", userName, userUuid, accountDetails.getAccount().getEmail(), accountDetails.getAccount().getUuid());

        try {
            User updatedUser = userService.updateUserNameByUserUuid(accountDetails.getAccount().getId(), userUuid,
                    userName);
            return ResponseEntity.ok(updatedUser);
        } catch (AccountNotFoundException ex) {
            log.atSevere().log("AccountNotFoundException was catched in updateUserNameForCurrentlyLoggedInAccountByUserUuid where " +
                    "it shouldn't be thrown, Account info [%s]", accountDetails.getAccount());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (UserNotFoundException | UserFoundException ex) {
            log.atFine().log("UserNotFoundException or UserFoundException was catched in " +
                    "updateUserNameForCurrentlyLoggedInAccountByUserUuid returning HTTP BAD_REQUEST(400)");
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping(params = {"status", "uuid"})
    public ResponseEntity<Boolean> updateUserStatusForCurrentlyLoggedInAccountByUserUuid(@AuthenticationPrincipal AccountDetails accountDetails, @RequestParam("status") @NotBlank String status, @RequestParam("uuid") @UUIDConstraint UUID userUuid) {
        log.atFinest().log("Request for User status [%s] updating with User UUID [%s] for currently logged in Account" +
                        "with Email [%s] and UUID [%s]", status, userUuid, accountDetails.getAccount().getEmail(),
                accountDetails.getAccount().getUuid());
        try {
            boolean resultOfStatusUpdating =
                    userService.updateUserStatusByUserUuid(accountDetails.getAccount().getId(), userUuid, status);
            return ResponseEntity.ok(true);
        } catch (AccountNotFoundException ex) {
            log.atSevere().log("AccountNotFoundException was catched in updateUserStatusForCurrentlyLoggedInAccountByUserUuid where " +
                    "it shouldn't be thrown, Account info [%s]", accountDetails.getAccount());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (UserNotFoundException | IllegalArgumentException ex) {
            log.atFine().log("UserNotFoundException or IllegalArgumentException was catched in " +
                    "updateUserStatusForCurrentlyLoggedInAccountByUserUuid returning HTTP BAD_REQUEST(400)");
            return ResponseEntity.badRequest().body(null);
        }
    }


    //TODO update users userName for support
    //TODO Add get Page<User> by given userName
    //TODO getAllUsersByAccountId (includes the deleted ones)
}
