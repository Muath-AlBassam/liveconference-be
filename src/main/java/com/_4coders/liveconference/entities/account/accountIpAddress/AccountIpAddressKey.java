package com._4coders.liveconference.entities.account.accountIpAddress;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

@Embeddable
@Data
public class AccountIpAddressKey implements Serializable {
    @Transient
    private static final long serialVersionUID = -24123675235823457L;

    private Long accountID;

    private Long ipAddressID;
}
