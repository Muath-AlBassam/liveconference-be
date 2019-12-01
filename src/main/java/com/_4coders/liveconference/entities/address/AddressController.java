package com._4coders.liveconference.entities.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flogger/address")
public class AddressController {
    @Autowired
    private AddressService addressService;
}
