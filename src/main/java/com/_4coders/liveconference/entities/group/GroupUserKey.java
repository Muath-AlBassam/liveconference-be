package com._4coders.liveconference.entities.group;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

@Embeddable
@Data
public class GroupUserKey implements Serializable {

    @Transient
    private static final long serialVersionUID = -5767123675235823457L;

    private Long userID;

    private Long groupID;

}
