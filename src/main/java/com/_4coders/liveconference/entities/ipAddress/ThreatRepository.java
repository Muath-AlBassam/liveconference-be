package com._4coders.liveconference.entities.ipAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreatRepository extends JpaRepository<Threat, Long> {

    boolean existsThreatByTorAndProxyAndAnonymousAndKnownAttackerAndKnownAbuserAndThreatAndBogon(
            boolean isTor, boolean isProxy, boolean IsAnonymous, boolean isKnownAttacker, boolean isKnownAbuser,
            boolean isThreat, boolean isBogon);

    Threat getThreatByTorAndProxyAndAnonymousAndKnownAttackerAndKnownAbuserAndThreatAndBogon(
            boolean isTor, boolean isProxy, boolean IsAnonymous, boolean isKnownAttacker, boolean isKnownAbuser,
            boolean isThreat, boolean isBogon);
}
