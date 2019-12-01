package com._4coders.liveconference.entities.group;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class GroupUserKey implements Serializable {

    private Long userID;

    private Long groupID;

}
