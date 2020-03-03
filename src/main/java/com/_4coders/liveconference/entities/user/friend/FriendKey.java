package com._4coders.liveconference.entities.user.friend;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

@Embeddable
@Data
public class FriendKey implements Serializable {

    @Transient
    private static final long serialVersionUID = -3021388765124L;

    private Long userAdderID;

    private Long userAddedID;
}
