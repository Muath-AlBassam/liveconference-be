package com._4coders.liveconference.entities.ipAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    boolean existsLanguageByNameAndNativeNameAndRtl(String name, String nativeName, boolean isRtl);

    Language getLanguageByNameAndNativeNameAndRtl(String name, String nativeName, boolean isRtl);
}
