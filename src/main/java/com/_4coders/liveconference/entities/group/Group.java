package com._4coders.liveconference.entities.group;


import com._4coders.liveconference.entities.channel.Channel;
import com._4coders.liveconference.entities.setting.group.GroupSetting;
import com._4coders.liveconference.entities.user.User;
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
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "groups")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Group extends RepresentationModel<Group> implements Serializable {

    @Transient
    private static final long serialVersionUID = 12489724167512314L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native_groups"
    )
    @GenericGenerator(
            name = "native_groups",
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

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_user_creator", referencedColumnName = "id", nullable = false, updatable = false)
    private User creator;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    private Date creationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Date lastModifiedDate;

    @OneToMany(mappedBy = "group")
    private Set<GroupUser> groupUsers;

    @OneToMany(mappedBy = "ownerGroup")
    private Set<Channel> channels;

    @OneToOne(mappedBy = "group", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private GroupSetting groupSetting;

    //TODO add default channel

    @PrePersist
    private void setUUID() {
        uuid = UUID.randomUUID();
    }
}
