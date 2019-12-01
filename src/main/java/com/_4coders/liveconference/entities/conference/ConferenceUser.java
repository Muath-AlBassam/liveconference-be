package com._4coders.liveconference.entities.conference;

import com._4coders.liveconference.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "conferences_users")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ConferenceUser extends RepresentationModel<ConferenceUser> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    @EqualsAndHashCode.Include
    private Long id;


    @OneToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_conference_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Conference conference;

    @Column(name = "entered_date", nullable = false, updatable = false)
    @CreatedDate
    private Date enteredDate;

    @Column(name = "left_date")
    private Date leftDate;

    @Column(name = "disconnection_type")
    private Type disconnectionType;


}
