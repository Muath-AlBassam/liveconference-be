package com._4coders.liveconference.entities.setting.group;

import com._4coders.liveconference.entities.group.Group;
import com._4coders.liveconference.entities.role.group.GroupRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * represents the settings for the {@code Group} that the creator or any onw that has the {@code Permission} to
 * modify can change
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 22/1/2020
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "group_settings")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class GroupSetting extends RepresentationModel<GroupSetting> implements Serializable {

    @Transient
    private static final long serialVersionUID = 7810120178401200987L;

    @Id
//    @Column(name = "id") //the name will be taken from groupID group_id
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Date lastModifiedDate;

    @OneToOne
    @MapsId
    private Group group;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "group_settings_group_roles",
            joinColumns = @JoinColumn(name = "fk_group_settings_id", referencedColumnName = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_group_roles_id", referencedColumnName = "id"))
    private Set<GroupRole> roles;
}
