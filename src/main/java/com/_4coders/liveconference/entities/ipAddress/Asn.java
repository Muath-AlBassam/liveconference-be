package com._4coders.liveconference.entities.ipAddress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ipAddress__asn")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Asn implements Serializable {
    @Transient
    private static final long serialVersionUID = 4070499213413210451L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;
    @Column(name = "asn", nullable = false, columnDefinition = "TEXT")
    private String asn;
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;
    @Column(name = "domain", nullable = false, columnDefinition = "TEXT")
    private String domain;
    @Column(name = "route", nullable = false, columnDefinition = "TEXT")
    private String route;
    @Column(name = "type", nullable = false)
    private AsnType type;
    @Column(name = "registration_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registrationDate;
    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Asn(String asn, String name, String domain, String route, AsnType type) {
        this.asn = asn;
        this.name = name;
        this.domain = domain;
        this.route = route;
        this.type = type;
    }
}
