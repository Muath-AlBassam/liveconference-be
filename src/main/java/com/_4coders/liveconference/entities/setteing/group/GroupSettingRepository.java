package com._4coders.liveconference.entities.setteing.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupSettingRepository extends JpaRepository<GroupSetting, Long> {
}
