package com._4coders.liveconference.entities.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Page<User> getUsersByUserNameStartsWith(String userName, Pageable pageable);


    Page<User> getUsersByUserNameStartsWith(
            @Param("requester_id") Long requesterId, @Param("username_to_look_for") String userNameToLookFor,
            @Param("to_order_by") String orderBy, Pageable pageable);

    Set<User> getUsersByAccount_EmailAndIsDeletedIsFalse(String email, Sort sort);

    Set<User> getUsersByAccount_UuidAndIsDeletedIsFalse(UUID uuid, Sort sort);

    Set<User> getUsersByAccount_IdAndIsDeletedIsFalse(Long id, Sort sort);

    User getUserByAccount_IdAndIsDeletedIsFalse(Long accountId);

    User getUserByUserNameAndAccount_IdAndIsDeletedIsFalse(String userName, Long accountId);

    User getUserByUserNameAndIsDeletedIsFalse(String userName);

    User getUserByUserName(String userName);

    User getUserByUuid(UUID uuid);

    User getUserByUuidAndIsDeletedIsFalse(UUID uuid);

    User getUserByUuidAndAccount_IdAndIsDeletedIsFalse(UUID uuid, Long accountId);

    boolean existsUserByUserNameAndIsDeletedIsFalse(String userName);

    boolean existsUserByUserName(String userName);

    boolean existsUserByUuid(UUID uuid);

    boolean existsUserByAccount_IdAndIsDeletedIsFalse(Long accountId);


    int countUsersByAccount_IdAndIsDeletedIsFalse(Long id);

    void deleteUserByAccount_IdAndIsDeletedIsFalse(Long accountId);


}
