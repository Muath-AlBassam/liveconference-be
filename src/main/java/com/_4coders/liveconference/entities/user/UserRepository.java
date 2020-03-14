package com._4coders.liveconference.entities.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

//    Page<User> getUsersByUserNameStartsWith(String userName, Pageable pageable);

    @Query(value = "select id,\n" +
            "       creation_date,\n" +
            "       is_deleted,\n" +
            "       last_login,\n" +
            "       last_modified_date,\n" +
            "       status,\n" +
            "       username,\n" +
            "       uuid,\n" +
            "       fk_account_id,\n" +
            "       last_status/*,*/\n" +//TODO FIND FIX
            "      " +
//            " (select case when (count(1) > 0) then true else false end\n" +
//            "       " +
//            " from accounts,\n" +
//            "             blocked_accounts ba\n" +
//            "        where accounts.id = ba.fk_account_blocker_id\n" +
//            "          and accounts.id = :requester_id and ba.fk_account_blocked_id = users.id) as isBlocked\n" +
            "from users\n" +
            "where username like concat(:username_to_look_for, '%') and username <> :requester_username",
            countQuery = "select count(1) from users where username like concat(:username_to_look_for, '%') and username <> :requester_username",
            nativeQuery = true)
    Page<User> getUsersByUserNameStartsWith(
            /*@Param("requester_id") Long requesterId,*/ @Param("username_to_look_for") String userNameToLookFor,
                                                         @Param("requester_username") String requesterName,
                                                         Pageable pageable);

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
