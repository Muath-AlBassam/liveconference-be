package com._4coders.liveconference.entities.user.friend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendKey> {

    boolean existsFriendByAdded_UserNameAndIsFriendIsTrue(String userName);

    boolean existsFriendByAdder_UserNameAndAdded_UserNameAndIsFriendIsTrue(String adderUserName, String addedUserName);

    Page<Friend> getFriendsByAdder_IdAndIsFriendIsTrue(Long adderId, Pageable pageable);
}
