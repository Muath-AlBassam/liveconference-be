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
@Table(name = "ipAddress__languages")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Language implements Serializable {
    @Transient
    private static final long serialVersionUID = 4070489213413210451L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_ipAddress__languages"
    )
    @GenericGenerator(
            name = "native_ipAddress__languages",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "native_name", columnDefinition = "TEXT")
    @JsonProperty("native")
    private String nativeName;

    @Column(name = "rtl")
    private boolean rtl;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registrationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Language(String name, String nativeName, boolean rtl) {
        this.name = name;
        this.nativeName = nativeName;
        this.rtl = rtl;
    }
}
