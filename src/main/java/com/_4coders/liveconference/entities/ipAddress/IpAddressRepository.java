package com._4coders.liveconference.entities.ipAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {

    IpAddress getIpAddressByIp(String ip);

    boolean existsIpAddressByIp(String ip);
}
