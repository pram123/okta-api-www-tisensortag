package com.okta.poc.iot.tisensor.apidynamodb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final CounterService counterService;

    @Autowired
    public MetricsService(CounterService cs) {
        this.counterService = cs;
    }

    public void exampleMethod() {
        this.counterService.increment("services.system.myservice.invoked");
    }


}
