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

    @Query(value = "select accounts.id,\n" +
            "       email,\n" +
            "       first_name,\n" +
            "       is_activated,\n" +
            "       last_login_date,\n" +
            "       last_logout_date,\n" +
            "       last_modified_date,\n" +
            "       last_name,\n" +
            "       middle_name,\n" +
            "       password,\n" +
            "       phone_number,\n" +
            "       registration_date,\n" +
            "       uuid,\n" +
            "       fk_default_user_id\n" +
            "from accounts\n" +
            "         left join system_blocked_accounts sba on accounts.id = sba.fk_account_id\n" +
            "where accounts.email = :email_to_look_for\n" +
            "  and ((sba.is_blocked = false and sba.is_permanent = false) or (sba.is_blocked = false and sba.is_permanent IS NULL)\n" +
            "    or (sba.is_blocked IS NULL and sba.is_permanent IS NULL));", nativeQuery = true)
    Account getAccountByEmailAndIsBlockedIsFalse(@Param("email_to_look_for") String email);

    Account getAccountByUuid(UUID uuid);

    boolean existsAccountByPhoneNumber(String phoneNumber);

    boolean existsAccountByEmail(String email);

    /**
     * Checks wither an {@link Account} exists by the given {@code Email} and {@code PhoneNumber}
     *
     * @return 0 if no {@link Account} exists with the given @code Email} and {@code PhoneNumber}<br/>
     * 1 if an {@link Account} exists with the given {@code email} only <br/>
     * 2 if an {@link Account} exists with the given {@code PhoneNumber} only <br/>
     * 3 if an {@link Account} exists with the given {@code Email} and {@code PhoneNumber}
     */
    @Query(value = "select case\n" +
            "           when (select count(1) > 0\n" +
            "                 from accounts\n" +
            "                 where accounts.email = :emailToCheck\n" +
            "                   and accounts.phone_number = :phoneNumberToCheck) then 3\n" +
            "           when (select count(1) > 0 from accounts where accounts.phone_number = :phoneNumberToCheck) then 2\n" +
            "           when (select count(1) > 0 from accounts where accounts.email = :emailToCheck) then 1\n" +
            "           else 0 end", nativeQuery = true)
    int existAccountByEmailOrPhoneNumber(@Param("emailToCheck") String emailToCheck,
                                         @Param("phoneNumberToCheck") String phoneNumberToCheck);

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
            "  and ((sba.is_blocked = false and sba.is_permanent = false) or (sba.is_blocked = false and sba.is_permanent IS NULL)\n" +
            "    or (sba.is_blocked IS NULL and sba.is_permanent IS NULL));\n", nativeQuery = true)
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
