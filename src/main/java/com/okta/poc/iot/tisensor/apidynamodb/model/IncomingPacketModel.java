package com.okta.poc.iot.tisensor.apidynamodb.model;

import java.util.ArrayList;

/*
This is what the incoming packet should look like
 */

public class IncomingPacketModel {

    String accessToken;
    ArrayList<SensorDataModel> sensorData;
    String deviceID;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ArrayList<SensorDataModel> getSensorData() {
        return sensorData;
    }

    public void setSensorData(ArrayList<SensorDataModel> sensorData) {
        this.sensorData = sensorData;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
