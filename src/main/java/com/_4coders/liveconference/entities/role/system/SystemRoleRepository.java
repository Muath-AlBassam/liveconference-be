package com._4coders.liveconference.entities.role.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {

    SystemRole getSystemRoleByName(String roleName);
}
