package com.okta.poc.iot.tisensor.apidynamodb;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {


    @PreAuthorize("hasRole('iotAdmin')")
    public boolean isIoTAdmin() {return true;}

    @PreAuthorize("hasRole('iotUser')")
    public boolean isIoTUser() {return true;}


}
