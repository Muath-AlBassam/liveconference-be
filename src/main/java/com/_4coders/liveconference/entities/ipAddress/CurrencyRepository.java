package com._4coders.liveconference.entities.ipAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    boolean existsCurrencyByNameAndCodeAndSymbolAndNativeNameAndPlural(String name, String code, String symbol,
                                                                       String nativeName, String plural);

    Currency getCurrencyByNameAndCodeAndSymbolAndNativeNameAndPlural(String name, String code, String symbol,
                                                                     String nativeName, String plural);
}
