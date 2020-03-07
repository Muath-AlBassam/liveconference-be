package com._4coders.liveconference.entities.user.friend;

import com._4coders.liveconference.entities.account.AccountDetails;
import com._4coders.liveconference.exception.account.AccountsBlockedException;
import com._4coders.liveconference.exception.sort.MappingSortPropertiesToSchemaPropertiesException;
import com._4coders.liveconference.exception.user.*;
import com._4coders.liveconference.validator.UUIDConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@RestController
@Flogger
@Validated
@RequestMapping("/flogger/users/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping(params = "uuid")
    public ResponseEntity<Boolean> friendRequest(@AuthenticationPrincipal AccountDetails accountDetails, @RequestParam(
            "uuid") @UUIDConstraint UUID toAddUuid) {
        if (accountDetails.getAccount().getCurrentInUseUser() == null) {
            return ResponseEntity.badRequest().body(false);
        } else {
            log.atFinest().log("Request for adding a friend with UUID [%s] and adder User UserName [%s] and UUID [%s]",
                    toAddUuid, accountDetails.getAccount().getCurrentInUseUser().getUserName(),
                    accountDetails.getAccount().getCurrentInUseUser().getUuid());
            try {
                boolean result = friendRequestService.friendRequest(accountDetails.getAccount(), toAddUuid);
                return ResponseEntity.ok(result);
            } catch (UserNotFoundException | BefriendSelfException | FriendAlreadyEstablishedException | AccountsBlockedException | FriendRequestAlreadyExist ex) {
                return ResponseEntity.badRequest().body(false);
            }
        }
    }

    @PostMapping(params = {"uuid", "response"})
    @Transactional
    public ResponseEntity<Boolean> friendRequestResponse(@AuthenticationPrincipal AccountDetails accountDetails, @RequestParam(
            "uuid") @UUIDConstraint UUID requesterUuid, @RequestParam("response") @NotBlank String response) {
        if (accountDetails.getAccount().getCurrentInUseUser() == null) {
            return ResponseEntity.badRequest().body(false);
        } else {
            log.atFinest().log("Request for responding to Friend request for requester with UUID [%s] and responder " +
                            "User UUID [%s] with response [%s]", requesterUuid,
                    accountDetails.getAccount().getCurrentInUseUser().getUuid(), response);
            try {
                boolean result =
                        friendRequestService.friendRequestResponse(accountDetails.getAccount().getCurrentInUseUser(),
                                requesterUuid, response);
                return ResponseEntity.ok(result);
            } catch (FriendRequestNotFoundException | UnknownResponseValueException | FriendAlreadyEstablishedException | AccountsBlockedException ex) {
                return ResponseEntity.badRequest().body(false);
            }
        }
    }

    @GetMapping
    @JsonView(FriendView.OwnerDetails.class)
    public ResponseEntity<Page<Friend>> getFriends(@AuthenticationPrincipal AccountDetails accountDetails,
                                                   Pageable pageable) {
        if (accountDetails.getAccount().getCurrentInUseUser() == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            log.atFinest().log("Request for getting Page<Friend> for user with UserName [%s], UUID [%s] and Page [%s]",
                    accountDetails.getAccount().getCurrentInUseUser().getUserName(),
                    accountDetails.getAccount().getCurrentInUseUser().getUuid(), pageable);
            try {
                Page<Friend> fetchedUsers =
                        friendService.getFriends(accountDetails.getAccount().getCurrentInUseUser(), pageable);
                return ResponseEntity.ok(fetchedUsers);
            } catch (MappingSortPropertiesToSchemaPropertiesException ex) {
                return ResponseEntity.badRequest().body(null);
            }
        }
    }

    @GetMapping("/requests")
    @JsonView(FriendView.OwnerDetails.class)
    public ResponseEntity<Page<FriendRequest>> getReceivedFriendRequests(@AuthenticationPrincipal AccountDetails accountDetails,
                                                                         Pageable pageable) {
        if (accountDetails.getAccount().getCurrentInUseUser() == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            log.atFinest().log("Request for getting Page<FriendRequest> for user with UserName [%s], UUID [%s] and Page [%s]",
                    accountDetails.getAccount().getCurrentInUseUser().getUserName(),
                    accountDetails.getAccount().getCurrentInUseUser().getUuid(), pageable);
            try {
                Page<FriendRequest> fetchedUserRequests =
                        friendRequestService.getFriendsRequests(accountDetails.getAccount().getCurrentInUseUser(), pageable);
                return ResponseEntity.ok(fetchedUserRequests);
            } catch (MappingSortPropertiesToSchemaPropertiesException ex) {
                return ResponseEntity.badRequest().body(null);
            }
        }
    }

}
//todo add delete friend
