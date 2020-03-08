package com._4coders.liveconference.entities.user.friend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendKey> {

    boolean existsFriendRequestByAdder_IdAndAdded_Id(Long adderId, Long addedId);

    FriendRequest getFriendRequestByAdder_UuidAndAdded_Uuid(UUID adder, UUID added);

    Page<FriendRequest> getFriendRequestsByAdded_Id(Long id, Pageable pageable);
}
