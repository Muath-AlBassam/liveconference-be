package com._4coders.liveconference.entities.ipAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsnRepository extends JpaRepository<Asn, Long> {

    boolean existsAsnByAsnAndNameAndDomainAndRouteAndType(String asn, String name, String domain, String route,
                                                          AsnType type);

    Asn getAsnByAsnAndNameAndDomainAndRouteAndType(String asn, String name, String domain, String route,
                                                   AsnType type);
}
