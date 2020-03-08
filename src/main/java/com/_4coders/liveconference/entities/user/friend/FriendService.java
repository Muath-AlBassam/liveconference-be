package com._4coders.liveconference.entities.user.friend;

import com._4coders.liveconference.entities.global.Checkers;
import com._4coders.liveconference.entities.global.Page;
import com._4coders.liveconference.entities.user.User;
import com._4coders.liveconference.entities.user.UserService;
import com._4coders.liveconference.exception.account.AccountsBlockedException;
import com._4coders.liveconference.exception.sort.MappingSortPropertiesToSchemaPropertiesException;
import com._4coders.liveconference.exception.user.FriendAlreadyEstablishedException;
import com._4coders.liveconference.util.sort.SortUtil;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
@Flogger
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private Checkers checkers;

    @Autowired
    private UserService userService;

    public void addFriend(User target, UUID requesterUserUuid) throws FriendAlreadyEstablishedException,
            AccountsBlockedException {
        User requester = userService.getUserByUUID(target.getUserName(), requesterUserUuid);
        if (requester.getIsFriend()) {
            log.atFinest().log("Throwing FriendAlreadyEstablishedException as the User with userName [%s] already is a friend " +
                    "with [%s]", target.getUserName(), requester.getUserName());
            throw new FriendAlreadyEstablishedException(String.format("User with userName [%s] already is a friend " +
                    "with [%s]", target.getUserName(), requester.getUserName()));
        } else {
            if (requester.getIsBlocked()) {
                log.atFinest().log("Throwing AccountsBlockedException as [%s] has blocked [%s]",
                        requester.getUserName(), target.getUserName());
                throw new AccountsBlockedException(String.format("[%s] has blocked [%s]",
                        requester.getUserName(), target.getUserName()));
            } else {
                Friend requesterTargetRelation = createFriend(requester, target);
                Friend targetRequesterRelation = createFriend(target, requester);
                friendRepository.saveAll(Arrays.asList(requesterTargetRelation, targetRequesterRelation));
            }
        }
    }

    //TODO RECHECK THIS
    public Page<Friend> getFriends(User requester, Pageable pageable) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Retrieving Page<Friend> for UserName [%s] and UUID [%s]", requester.getUserName(),
                requester.getUuid());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                SortUtil.userFriendMapping(pageable.getSort(), false));
        return new Page<>(friendRepository.getFriendsByAdder_IdAndIsFriendIsTrue(requester.getId(), pageable), pageable);
    }

    private Friend createFriend(User requester, User target) {
        Friend friend = new Friend();
        FriendKey friendKey = new FriendKey();
        friendKey.setUserAdderID(requester.getId());
        friendKey.setUserAddedID(target.getId());
        friend.setFriendKey(friendKey);
        friend.setAdder(requester);
        friend.setAdded(target);
        friend.setIsFriend(true);
        friend.setRelationStartDate(LocalDateTime.now());
        return friend;
    }
}
