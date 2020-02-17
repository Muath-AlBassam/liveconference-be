package com._4coders.liveconference.entities.ipAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationInformationRepository extends JpaRepository<LocationInformation, Long> {

    boolean existsLocationInformationByEuAndCityAndRegionAndRegionCodeAndCountryNameAndCountryCodeAndContinentNameAndContinentCodeAndCallingCodeAndTimeZoneNameAndTimeZoneAbbreviationAndTimeZoneOffset(
            boolean isEu, String city, String Region, String regionCode, String countryName, String countryCode,
            String continentName, String continentCode, String callingCode, String timeZoneName,
            String timeZoneAbbreviation, String timeZoneOffset);

    LocationInformation getLocationInformationByEuAndCityAndRegionAndRegionCodeAndCountryNameAndCountryCodeAndContinentNameAndContinentCodeAndCallingCodeAndTimeZoneNameAndTimeZoneAbbreviationAndTimeZoneOffset(
            boolean isEu, String city, String Region, String regionCode, String countryName, String countryCode,
            String continentName, String continentCode, String callingCode, String timeZoneName,
            String timeZoneAbbreviation, String timeZoneOffset);
}
