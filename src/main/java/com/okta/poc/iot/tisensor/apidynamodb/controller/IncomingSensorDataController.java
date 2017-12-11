package com.okta.poc.iot.tisensor.apidynamodb.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nimbusds.oauth2.sdk.ParseException;
import com.okta.poc.iot.tisensor.apidynamodb.component.IncomingDataComponent;
import com.okta.poc.iot.tisensor.apidynamodb.component.OutgoingDataComponent;
import com.okta.poc.iot.tisensor.apidynamodb.model.ResponseModel;
import com.okta.poc.iot.tisensor.apidynamodb.repository.AccelerometerRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.GyroscopeRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.SensorRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.UserModelRepository;
import com.okta.poc.iot.tisensor.apidynamodb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/rest/incoming")
public class IncomingSensorDataController {

    private static Logger log = LoggerFactory.getLogger(IncomingSensorDataController.class);

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    AccelerometerRepository accelerometerRepository;

    @Autowired
    GyroscopeRepository gyroscopeRepository;

    @Autowired
    OutgoingDataComponent outgoingDataComponent;

    @Autowired
    UserService userService;

    @Autowired
    UserModelRepository userModelRepository;

    //   @ResponseStatus( HttpStatus.OK )
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes="application/json",
            produces = "application/json")
    public ResponseEntity<String> incoming(@RequestBody JsonNode incomingPacket) throws IOException, ParseException {
        log.debug("Got info:"+ incomingPacket);
       // log.debug("Got accessToken {} for deviceID: {}", incomingPacket.getAccessToken(),incomingPacket.getDeviceID() );
        //log.debug("Got info for deviceID:" + incomingPacket.getDeviceID());
        IncomingDataComponent incomingDataComponent = new IncomingDataComponent(sensorRepository,
                accelerometerRepository,gyroscopeRepository,outgoingDataComponent, userModelRepository);

        ResponseModel msg = incomingDataComponent.saveIncoming(incomingPacket);
        return new ResponseEntity<>(msg.responseShortMsg, msg.responseCode);
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST, consumes="application/json",
            produces = "application/json")
    public ResponseEntity<String> initialConfiguration(@RequestBody JsonNode init) throws IOException, ParseException {
        log.debug("Got info:"+ init);
        ResponseModel msg = new ResponseModel();
        msg.responseShortMsg = userService.init(init).toString();
        msg.responseCode = HttpStatus.OK;
        return new ResponseEntity<>(msg.responseShortMsg, msg.responseCode);
    }
}
