package com._4coders.liveconference.entities.ipAddress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "ipAddress__threats")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Threat implements Serializable {

    @Transient
    private static final long serialVersionUID = 2489213413210451L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_ipAddress__threats"
    )
    @GenericGenerator(
            name = "native_ipAddress__threats",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @JsonProperty("is_tor")
    @Column(name = "is_tor", nullable = false)
    private Boolean tor;

    @JsonProperty("is_proxy")
    @Column(name = "is_proxy", nullable = false)
    private Boolean proxy;

    @JsonProperty("is_anonymous")
    @Column(name = "is_anonymous", nullable = false)
    private Boolean anonymous;

    @JsonProperty("is_known_attacker")
    @Column(name = "is_known_attacker", nullable = false)
    private Boolean knownAttacker;

    @JsonProperty("is_known_abuser")
    @Column(name = "is_known_abuser", nullable = false)
    private Boolean knownAbuser;

    @JsonProperty("is_threat")
    @Column(name = "is_threat", nullable = false)
    private Boolean threat;

    @JsonProperty("is_bogon")
    @Column(name = "is_bogon", nullable = false)
    private Boolean bogon;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registrationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Threat(Boolean tor, Boolean proxy, Boolean anonymous, Boolean knownAttacker, Boolean knownAbuser, Boolean threat, Boolean bogon) {
        this.tor = tor;
        this.proxy = proxy;
        this.anonymous = anonymous;
        this.knownAttacker = knownAttacker;
        this.knownAbuser = knownAbuser;
        this.threat = threat;
        this.bogon = bogon;
    }
}
