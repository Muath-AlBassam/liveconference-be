package com._4coders.liveconference.entities.ipAddress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ipAddress__location_information")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LocationInformation implements Serializable {
    @Transient
    private static final long serialVersionUID = 1800489213724210451L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_ipAddress__location_information"
    )
    @GenericGenerator(
            name = "native_ipAddress__location_information",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;
    @Column(name = "eu", nullable = false)
    private boolean eu;
    @Column(name = "city", columnDefinition = "TEXT")
    private String city;
    @Column(name = "region", columnDefinition = "TEXT")
    private String region;
    @Column(name = "region_code", columnDefinition = "TEXT")
    private String regionCode;
    @Column(name = "country_name", nullable = false, columnDefinition = "TEXT")
    private String countryName;
    @Column(name = "country_code", nullable = false, columnDefinition = "TEXT")
    private String countryCode;
    @Column(name = "continent_name", nullable = false, columnDefinition = "TEXT")
    private String continentName;
    @Column(name = "continent_code", nullable = false, columnDefinition = "TEXT")
    private String continentCode;
    @Column(name = "calling_code", nullable = false, columnDefinition = "TEXT")
    private String callingCode;
    @Column(name = "time_zone_name", nullable = false, columnDefinition = "TEXT")
    private String timeZoneName;
    @Column(name = "time_zone_abbreviation", nullable = false, columnDefinition = "TEXT")
    private String timeZoneAbbreviation;
    @Column(name = "time_zone_offset", nullable = false, columnDefinition = "TEXT")
    private String timeZoneOffset;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registrationDate;
    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public LocationInformation(boolean eu, String city, String region, String regionCode, String countryName, String countryCode, String continentName, String continentCode, String callingCode, String timeZoneName, String timeZoneAbbreviation, String timeZoneOffset) {
        this.eu = eu;
        this.city = city;
        this.region = region;
        this.regionCode = regionCode;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.continentName = continentName;
        this.continentCode = continentCode;
        this.callingCode = callingCode;
        this.timeZoneName = timeZoneName;
        this.timeZoneAbbreviation = timeZoneAbbreviation;
        this.timeZoneOffset = timeZoneOffset;
    }
}
