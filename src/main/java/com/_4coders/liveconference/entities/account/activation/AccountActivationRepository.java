package com._4coders.liveconference.entities.account.activation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AccountActivationRepository extends JpaRepository<AccountActivation, Long> {

    boolean existsAccountActivationByAccountToActivate_IdAndActivatedIsNullAndExpiryDateAfter(Long accountId, LocalDateTime dateTime);

    AccountActivation getAccountActivationByAccountToActivate_IdAndActivatedIsNullAndExpiryDateAfter(Long accountId, LocalDateTime dateTime);
}
