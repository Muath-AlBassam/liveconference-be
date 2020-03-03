package com._4coders.liveconference.entities.user.friend;

import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Maps the relation between 2 {@link User}s
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 01/03/2020
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "friends")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Friend extends RepresentationModel<Friend> implements Serializable {

    @Transient
    private static final long serialVersionUID = 1223021388765124L;

    @EmbeddedId
    @EqualsAndHashCode.Include
    private FriendKey friendKey;

    @ManyToOne
    @MapsId("userAdderID")
    @JoinColumn(name = "fk_user_adder_id", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonIgnore
    private User adder;

    @ManyToOne
    @MapsId("userAddedID")
    @JoinColumn(name = "fk_user_added_id", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonView({FriendView.OwnerDetails.class, FriendView.Others.class})
    private User added;


    @Column(name = "friend", nullable = false)
    @JsonView({FriendView.OwnerDetails.class})
    private Boolean isFriend;

//    @Transient
//    private Boolean blocked;

    @Column(name = "relation_start_date", nullable = false)
    @JsonView({FriendView.OwnerDetails.class})
    private LocalDateTime relationStartDate;

    @Column(name = "relation_end_date")
    private LocalDateTime relationEndDate;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
