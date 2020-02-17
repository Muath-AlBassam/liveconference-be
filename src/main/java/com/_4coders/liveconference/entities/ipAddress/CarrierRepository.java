package com._4coders.liveconference.entities.ipAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    boolean existsCarrierByNameAndMccAndMnc(String name, String mcc, String mnc);

    Carrier getCarrierByNameAndMccAndMnc(String name, String mcc, String mnc);
}
