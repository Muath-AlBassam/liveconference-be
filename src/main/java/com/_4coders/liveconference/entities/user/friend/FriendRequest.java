package com._4coders.liveconference.entities.user.friend;

import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Before having a {@link Friend} a request for friendship must be sent, this where this class comes
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 02/03/2020
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "friend_request")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class FriendRequest extends RepresentationModel<FriendRequest> implements Serializable {

    @Transient
    private static final long serialVersionUID = 1064021388765124L;

    @EmbeddedId
    @EqualsAndHashCode.Include
    private FriendKey friendKey;


    @ManyToOne
    @MapsId("userAdderID")
    @JoinColumn(name = "fk_user_adder_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User adder;

    @ManyToOne
    @MapsId("userAddedID")
    @JoinColumn(name = "fk_user_added_id", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonView({FriendView.OwnerDetails.class, FriendView.Others.class})
    private User added;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    @JsonView({FriendView.OwnerDetails.class, FriendView.Others.class})
    private Date creationDate;
}
