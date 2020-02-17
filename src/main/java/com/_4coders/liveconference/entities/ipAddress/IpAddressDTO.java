package com._4coders.liveconference.entities.ipAddress;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class IpAddressDTO {

    private String ip;
    @JsonProperty("is_eu")
    private boolean eu;
    private String city;
    private String region;
    @JsonProperty("region_code")
    private String regionCode;
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("continent_name")
    private String continentName;
    @JsonProperty("continent_code")
    private String continentCode;
    private double latitude;
    private double longitude;
    private String organization;
    private String postal;
    @JsonProperty("calling_code")
    private String callingCode;
    private int count;
    @JsonProperty("flag")
    private String flagLink;
    @JsonProperty("emoji_flag")
    private String emojiFlag;
    @JsonProperty("emoji_unicode")
    private String emojiUnicode;

    private String timeZoneName;
    private String timeZoneAbbreviation;
    private String timeZoneOffset;

    private Asn asn;
    private Carrier carrier;
    private Set<Language> languages;
    private Currency currency;

    private Threat threat;


    public IpAddress toIpAddress() {
        LocationInformation locationInformation = new LocationInformation(eu, city, region, regionCode, countryName,
                countryCode, continentName, continentCode, callingCode, timeZoneName, timeZoneAbbreviation, timeZoneOffset);
        IpAddress toReturn = new IpAddress();
        toReturn.setLocationInformation(locationInformation);
        toReturn.setIp(ip);
        toReturn.setLatitude(latitude);
        toReturn.setLongitude(longitude);
        toReturn.setPostal(postal);
        toReturn.setCount(count);
        toReturn.setFlagLink(flagLink);
        toReturn.setEmojiFlag(emojiFlag);
        toReturn.setEmojiUnicode(emojiUnicode);
        toReturn.setAsn(asn);
        toReturn.setCarrier(carrier);
        toReturn.setLanguages(languages);
        toReturn.setCurrency(currency);
        toReturn.setThreat(threat);
        if (asn != null) {
            toReturn.setOrganization(organization == null ? asn.getName() : organization);

        } else {
            toReturn.setOrganization(organization);
        }
        return toReturn;
    }

    @JsonProperty("time_zone")
    private void unpackTimeZone(Map<String, Object> timeZoneObj) {
        timeZoneName = (String) timeZoneObj.get("name");
        timeZoneAbbreviation = (String) timeZoneObj.get("abbr");
        timeZoneOffset = (String) timeZoneObj.get("offset");
    }
}
