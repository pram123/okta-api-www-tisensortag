package com.okta.poc.iot.tisensor.apidynamodb.model;

import org.springframework.http.HttpStatus;


public class ResponseModel {
    public String responseShortMsg;
    public HttpStatus responseCode;
    public String responseLink;
}
