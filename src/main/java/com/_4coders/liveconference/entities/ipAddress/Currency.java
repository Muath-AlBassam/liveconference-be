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
@Table(name = "ipAddress__currency")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Currency implements Serializable {


    @Transient
    private static final long serialVersionUID = 1010489213413210451L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_ipAddress__currency"
    )
    @GenericGenerator(
            name = "native_ipAddress__currency",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;


    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;


    @Column(name = "code", nullable = false, columnDefinition = "TEXT")
    private String code;


    @Column(name = "symbol", nullable = false, columnDefinition = "TEXT")
    private String symbol;


    @JsonProperty("native")
    @Column(name = "native_name", nullable = false, columnDefinition = "TEXT")
    private String nativeName;


    @Column(name = "plural", nullable = false, columnDefinition = "TEXT")
    private String plural;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registrationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Currency(String name, String code, String symbol, String nativeName, String plural) {
        this.name = name;
        this.code = code;
        this.symbol = symbol;
        this.nativeName = nativeName;
        this.plural = plural;
    }
}
