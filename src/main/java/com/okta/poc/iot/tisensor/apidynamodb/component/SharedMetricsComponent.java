package com.okta.poc.iot.tisensor.apidynamodb.component;

import com.okta.poc.iot.tisensor.apidynamodb.model.AccelerometerDataModel;
import com.okta.poc.iot.tisensor.apidynamodb.model.GyroscopeDataModel;
import com.okta.poc.iot.tisensor.apidynamodb.model.SensorDataModel;
import org.springframework.stereotype.Component;

/*
    We will use this component as a way to quickly retrieve only the last
    information saved. That's b/c DynamoDB doesn't have a good way
    to just get the last X elements..
    So - why are we using dynamodb then? Simple - to store historical information
    so that we capture and graph it in v2!
 */
@Component
public class SharedMetricsComponent {

    //Buffer buf = new CircularFifoBuffer(4);

    AccelerometerDataModel accelerometerDataModel;
    GyroscopeDataModel gyroscopeDataModel;
    int numPacketsSaved;
    String deviceMoniterd;
    SensorDataModel savedSensorDataModel;

    public SensorDataModel getSavedSensorDataModel() {
        return savedSensorDataModel;
    }

    public void setSavedSensorDataModel(SensorDataModel savedSensorDataModel) {
        this.savedSensorDataModel = savedSensorDataModel;
    }

    public AccelerometerDataModel getAccelerometerDataModel() {
        return accelerometerDataModel;
    }

    public void setAccelerometerDataModel(AccelerometerDataModel accelerometerDataModel) {
        this.accelerometerDataModel = accelerometerDataModel;
    }

    public GyroscopeDataModel getGyroscopeDataModel() {
        return gyroscopeDataModel;
    }

    public void setGyroscopeDataModel(GyroscopeDataModel gyroscopeDataModel) {
        this.gyroscopeDataModel = gyroscopeDataModel;
    }

    public int getNumPacketsSaved() {
        return numPacketsSaved;
    }

    public void setNumPacketsSaved(int numPacketsSaved) {
        this.numPacketsSaved = numPacketsSaved;
    }

    public String getDeviceMoniterd() {
        return deviceMoniterd;
    }

    public void setDeviceMoniterd(String deviceMoniterd) {
        this.deviceMoniterd = deviceMoniterd;
    }
}
