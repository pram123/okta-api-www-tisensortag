package com.okta.poc.iot.tisensor.apidynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.stereotype.Repository;

@DynamoDBDocument
@Repository
@DynamoDBTable(tableName = "SensorData")
public class SensorDataModel {


    Double tempInfo;

    public Double getBarInfo() {
        return barInfo;
    }

    public void setBarInfo(Double barInfo) {
        this.barInfo = barInfo;
    }

    Double barInfo;
    long dataCaptureTime;


    public void setDataCaptureTime(long dataCaptureTime) {
        this.dataCaptureTime = dataCaptureTime;
    }

    public Double getTempInfo() {
        return tempInfo;
    }

    public void setTempInfo(Double tempInfo) {
        this.tempInfo = tempInfo;
    }




}
