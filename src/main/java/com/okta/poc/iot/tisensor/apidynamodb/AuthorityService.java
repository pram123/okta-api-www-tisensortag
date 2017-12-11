package com.okta.poc.iot.tisensor.apidynamodb;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {


    @PreAuthorize("hasRole('EmployeeAdmin')")
    public boolean isEmployeeAdmin() {return true;}


}
