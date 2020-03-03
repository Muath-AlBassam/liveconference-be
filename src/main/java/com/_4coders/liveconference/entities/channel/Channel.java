package com._4coders.liveconference.entities.channel;

import com._4coders.liveconference.entities.conference.Conference;
import com._4coders.liveconference.entities.group.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "channels")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Channel extends RepresentationModel<Channel> implements Serializable {

    @Transient
    private static final long serialVersionUID = -784459713871244L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_channels"
    )
    @GenericGenerator(
            name = "native_channels",
            strategy = "native"
    )
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String name;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    private Date creationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Date lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "fk_group_owner", referencedColumnName = "id", nullable = false, updatable = false)
    private Group ownerGroup;

    @OneToOne(mappedBy = "ownerChannel")
    private Conference conference;

//    @OneToMany
//    @JoinColumn(name = "")
//    private Set<Message> messages;//TODO: change to Page(must) in service

    //TODO add channel settings and conference

    @PrePersist
    private void setUUID() {
        uuid = UUID.randomUUID();
    }
}
