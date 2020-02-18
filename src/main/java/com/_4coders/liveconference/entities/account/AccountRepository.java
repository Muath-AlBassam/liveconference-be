package com._4coders.liveconference.entities.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account getAccountByEmail(String email);

    Account getAccountByUuid(UUID uuid);

    boolean existsAccountByPhoneNumber(String phoneNumber);

    boolean existsAccountByEmail(String email);

    boolean existsAccountByEmailOrPhoneNumber(String email, String phoneNumber);//may user native to get a number
    // indicating wither the error cause from email or number or non

    boolean existsAccountByUuid(UUID uuid);

    @Query(value = "select case\n" +
            "           when count(1) > 0 then false\n" +
            "           when (select count(1) from accounts where accounts.id = :account_to_lock_for) > 0 then true\n" +
            "           else false end\n" +
            "from accounts\n" +
            "         left join system_blocked_accounts sba on accounts.id = sba.fk_account_id\n" +
            "where accounts.id = :account_to_lock_for\n" +
            "  and accounts.is_activated = true\n" +
            "  and (sba.is_blocked = false and sba.is_permanent = false);\n", nativeQuery = true)
    boolean existsAccountByIdAndIsActivatedIsTrueAndIsBlockedIsFalse(@Param("account_to_lock_for") Long accountId);

    boolean existsAccountByIdAndIsActivatedIsTrue(Long accountId);


    @Modifying
    @Query(value = "update accounts set last_login_date = :new_date where id = :id_toFind", nativeQuery = true)
    void updateLastLoginDate(@Param("id_toFind") Long id, @Param("new_date") LocalDateTime date);

    @Modifying
    @Query(value = "update accounts set last_logout_date = :new_date where id = :id_toFind", nativeQuery = true)
    void updateLastLogoutDate(@Param("id_toFind") Long id, @Param("new_date") LocalDateTime date);

    @Modifying
    @Query(value = "update accounts set password = :pass where id = :id_toFind", nativeQuery = true)
    void updatePasswordById(@Param("id_toFind") Long id, @Param("pass") String password);

    @Modifying
    @Query(value = "update accounts set password = :pass where uuid = :uuid_toFind", nativeQuery = true)
    void updatePasswordByUuid(@Param("uuid_toFind") UUID uuid, @Param("pass") String password);

    @Modifying
    @Query(value = "update accounts set email = :new_email where id = :id_toFind", nativeQuery = true)
    void updateEmailById(@Param("id_toFind") Long id, @Param("new_email") String email);

    @Modifying
    @Query(value = "update accounts set email = :new_email where uuid = :uuid_toFind", nativeQuery = true)
    void updateEmailByUuid(@Param("uuid_toFind") UUID uuid, @Param("new_email") String email);
}
