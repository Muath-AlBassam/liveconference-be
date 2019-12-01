package com._4coders.liveconference.entities.group;

import com._4coders.liveconference.entities.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "groups_users")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class GroupUser extends RepresentationModel<GroupUser> {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private GroupUserKey key;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private User user;

    @ManyToOne
    @MapsId("groupID")
    @JoinColumn(name = "fk_group_id", referencedColumnName = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Group group;

    //TODO added the permission list

    @Column(name = "join_date", nullable = false, updatable = false)
    @CreatedDate
    private Date joinDate;

    @Column(name = "left_date", nullable = false)
    @LastModifiedDate
    private Date leftDate;

    @Column(name = "is_member", nullable = false)
    private Boolean isMember;

}
