package com.okta.poc.iot.tisensor.apidynamodb.controller;


import com.okta.poc.iot.tisensor.apidynamodb.model.AccelerometerDataModel;
import com.okta.poc.iot.tisensor.apidynamodb.model.GyroscopeDataModel;
import com.okta.poc.iot.tisensor.apidynamodb.model.SensorPacketModel;
import com.okta.poc.iot.tisensor.apidynamodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/user/{userId}")
public class UserController {



    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="/deviceID/{deviceID}", method = RequestMethod.PUT,  produces = "application/json")
    void addDeviceToUserProfile(@PathVariable String userId, @PathVariable String deviceId){
        userService.addDeviceID(userId,deviceId);
    }

    @RequestMapping(value = "/dump", method = RequestMethod.GET, produces = "application/json")
    ArrayList<SensorPacketModel> getAllInfo(@PathVariable String userId) {
        return this.userService.getAllMyInfo(userId);
    }


    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    void deleteMe(@PathVariable String userId,UserService userService){
        System.out.println("Wanted to delete for " + userId);

        this.userService.deleteUser(userId);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.DELETE)
    void deleteMe(@PathVariable String userId,String deviceId, UserService userService){
        System.out.println("Wanted to deleve for " + userId);
          this.userService.deleteUserDevice(userId,deviceId);
    }


    @RequestMapping(value="/{deviceId}/latest/sensorData", method = RequestMethod.GET, produces = "application/json")
    Object getLatestSensorData(@PathVariable String userId,@PathVariable String deviceId) {
        return userService.getLatestSensorData(userId,deviceId);
    }

    /* Accelerometer */
    @RequestMapping(value="/{deviceId}/latest/acc/{timeInMs}", method = RequestMethod.GET, produces = "application/json")
    List<AccelerometerDataModel> getLatest(@PathVariable String userId,@PathVariable String deviceId,
                                            @PathVariable String timeInMs) {
        return userService.getLatestAccData(userId,deviceId,timeInMs);
    }

    @RequestMapping(value="/{deviceId}/dump/acc", method = RequestMethod.GET, produces = "application/json")
    List<AccelerometerDataModel> getAccDump(@PathVariable String userId,@PathVariable String deviceId) {
        return userService.dumpAcc(userId, deviceId);
    }


    @RequestMapping(value="/{deviceId}/dump/gyro", method = RequestMethod.GET, produces = "application/json")
    List<GyroscopeDataModel> getGyroDump(@PathVariable String userId,@PathVariable String deviceId) {
        return userService.dumpGyro(userId, deviceId);
    }



}
