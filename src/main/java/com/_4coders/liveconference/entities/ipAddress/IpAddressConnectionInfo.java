package com._4coders.liveconference.entities.ipAddress;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class IpAddressConnectionInfo {

    private final String URL;
    private final String GET_IP_INFO_FULL_URL;
    private final String API_KEY;
    private final String API_KEY_STRING;


}
