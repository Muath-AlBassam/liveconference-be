package com._4coders.liveconference.entities.role.system;

import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Flogger
public class SystemRoleService {

    @Autowired
    private SystemRoleRepository systemRoleRepository;

    public SystemRole getRoleByName(String roleName) {
        log.atFinest().log("Retrieving SystemRole with roleName [%s]", roleName);
//        SystemRole toReturn = systemRoleRepository.getSystemRoleByName(roleName);
//        log.atFinest().log("SystemRole retrieved with values: [%s]",toReturn);
        return systemRoleRepository.getSystemRoleByName(roleName);
    }
}
