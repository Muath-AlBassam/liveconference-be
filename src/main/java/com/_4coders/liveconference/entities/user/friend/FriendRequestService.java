package com._4coders.liveconference.entities.user.friend;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.global.Checkers;
import com._4coders.liveconference.entities.user.User;
import com._4coders.liveconference.entities.user.UserService;
import com._4coders.liveconference.exception.account.AccountsBlockedException;
import com._4coders.liveconference.exception.sort.MappingSortPropertiesToSchemaPropertiesException;
import com._4coders.liveconference.exception.user.*;
import com._4coders.liveconference.util.sort.SortUtil;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Flogger
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    @Autowired
    private Checkers checkers;

    /**
     * Creates new {@link FriendRequest} between the given {@link Account} current {@link User} and the given
     * {@link UUID} of the target {@link User}
     *
     * @param requester the requester of the relation
     * @param toAdd     the {@link UUID} of the target
     * @return {@code true} if the {@link FriendRequest} was created
     * @throws UserNotFoundException             when no {@link User} exists with the given {@link UUID}
     * @throws BefriendSelfException             when the {@link User} tries to befriend himself
     * @throws FriendAlreadyEstablishedException when there exists a friendship relation between the given users
     * @throws AccountsBlockedException          when the {@code target} of this relation has blocked the initiator
     * @throws FriendRequestAlreadyExist         when a {@link FriendRequest} already exists
     */
    public boolean friendRequest(Account requester, UUID toAdd) throws UserNotFoundException, BefriendSelfException,
            FriendAlreadyEstablishedException, AccountsBlockedException, FriendRequestAlreadyExist {
        User fetchedUser = userService.getUserByUUIDAndIsDeletedIsFalse(requester, toAdd);
        if (requester.getUsers().stream().anyMatch(user -> user.getUuid().equals(toAdd))) {
            log.atFinest().log("Throwing BefriendSelfException as the User [%s] it trying to befriend himself [%s]",
                    requester.getCurrentInUseUser().getUuid(), toAdd);
            throw new BefriendSelfException(String.format("User [%s] it trying to befriend himself [%s]",
                    requester.getCurrentInUseUser().getUuid(), toAdd), toAdd);
        } else if (fetchedUser.getIsFriend()) {
            log.atFinest().log("Throwing FriendAlreadyEstablishedException as [%s] and [%s] are already friends",
                    requester.getCurrentInUseUser().getUserName(), fetchedUser.getUserName());
            throw new FriendAlreadyEstablishedException(String.format("[%s] and [%s] are already friends",
                    requester.getCurrentInUseUser().getUserName(), fetchedUser.getUserName()));
        } else {
//            if (checkers.checkIfTargetUserBlockedRequester(requester, fetchedUser)) {//CAN'T add
//                log.atFinest().log("Throwing AccountsBlockedException as [%s] has blocked [%s]",
//                        fetchedUser.getUserName(), requester.getCurrentInUseUser().getUserName());
//                throw new AccountsBlockedException(String.format("[%s] has blocked [%s]",
//                        fetchedUser.getUserName(), requester.getCurrentInUseUser().getUserName()));
            if (fetchedUser.getIsBlocked()) {
                log.atFinest().log("Throwing AccountsBlockedException as [%s] has blocked [%s]",
                        fetchedUser.getUserName(), requester.getCurrentInUseUser().getUserName());
                throw new AccountsBlockedException(String.format("[%s] has blocked [%s]",
                        fetchedUser.getUserName(), requester.getCurrentInUseUser().getUserName()));
            } else {
                if (friendRequestRepository.existsFriendRequestByAdder_IdAndAdded_Id(requester.getCurrentInUseUser().getId(), fetchedUser.getId())) {
                    log.atFinest().log("Throwing FriendRequestAlreadyExist as there exist a request between [%s] and " +
                            "[%s]", requester.getCurrentInUseUser().getUserName(), fetchedUser.getUserName());
                    throw new FriendRequestAlreadyExist(String.format("A request between [%s] and " +
                            "[%s] exists", requester.getCurrentInUseUser().getUserName(), fetchedUser.getUserName()));
                } else {
                    FriendRequest toSave = new FriendRequest();
                    FriendKey friendKey = new FriendKey();
                    friendKey.setUserAdderID(requester.getCurrentInUseUser().getId());
                    friendKey.setUserAddedID(fetchedUser.getId());
                    toSave.setFriendKey(friendKey);
                    toSave.setAdder(requester.getCurrentInUseUser());
                    toSave.setAdded(fetchedUser);
                    friendRequestRepository.save(toSave);
                    return true;
                }
            }
        }
    }


    public boolean friendRequestResponse(User target, UUID requesterUuid, String response) throws FriendRequestNotFoundException,
            UnknownResponseValueException, FriendAlreadyEstablishedException, AccountsBlockedException {
        FriendRequest fetchedRequest =
                friendRequestRepository.getFriendRequestByAdder_UuidAndAdded_Uuid(requesterUuid, target.getUuid());
        if (fetchedRequest == null) {
            log.atFinest().log("Throwing FriendRequestNotFoundException as No FriendRequest with adder [%s] and adder [%s] " +
                    "was found", requesterUuid, target.getUuid());
            throw new FriendRequestNotFoundException(String.format("No FriendRequest with adder [%s] and adder [%s] " +
                    "was found", requesterUuid, target.getUuid()), requesterUuid, target.getUuid());
        } else {
            switch (response) {
                case "accept":
                    friendService.addFriend(target, requesterUuid);
                    friendRequestRepository.delete(fetchedRequest);
                    break;//we can remove this line and the above one but nah
                case "reject":
                    friendRequestRepository.delete(fetchedRequest);
                    break;
                default:
                    log.atFinest().log("Throwing UnknownResponseValueException as the given response [%s] is unknown",
                            response);
                    throw new UnknownResponseValueException(String.format("Unknown response value [%s]", response),
                            response);
            }
            return true;
        }
    }

    public Page<FriendRequest> getFriendsRequests(User requester, Pageable pageable) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Retrieving Page<FriendRequest> for UserName [%s] and UUID [%s]", requester.getUserName(),
                requester.getUuid());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                SortUtil.userFriendRequestMapping(pageable.getSort(), false));
        return friendRequestRepository.getFriendRequestsByAdder_Id(requester.getId(), pageable);

    }
}
