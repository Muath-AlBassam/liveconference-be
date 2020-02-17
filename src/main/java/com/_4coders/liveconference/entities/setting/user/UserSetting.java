package com._4coders.liveconference.entities.setting.user;

import com._4coders.liveconference.entities.user.User;
import com._4coders.liveconference.entities.user.UserViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * represents the settings for the {@code User} that the {@code User} can change
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 22/1/2020
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_settings")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserSetting extends RepresentationModel<UserSetting> implements Serializable {

    @Transient
    private static final long serialVersionUID = -8777120000056446168L;

    @Id
    //    @Column(name = "id") //the name will be taken from userID user_id
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    @JsonView({UserViews.OwnerDetails.class})
    private Date lastModifiedDate;

    @OneToOne(optional = false)
    @MapsId
    @ToString.Exclude
    private User user;
}
