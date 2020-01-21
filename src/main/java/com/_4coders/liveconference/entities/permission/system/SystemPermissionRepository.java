package com._4coders.liveconference.entities.permission.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemPermissionRepository extends JpaRepository<SystemPermission, Long> {
}
