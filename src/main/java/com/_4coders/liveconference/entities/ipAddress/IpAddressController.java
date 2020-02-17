package com._4coders.liveconference.entities.ipAddress;

import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Flogger
@RequestMapping(value = "/flogger/ipAddress", produces = {"application/json", "application/hal+json"})
public class IpAddressController {

    @Autowired
    private IpAddressService ipAddressService;
}
