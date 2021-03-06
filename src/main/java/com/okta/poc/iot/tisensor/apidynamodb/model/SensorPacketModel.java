package com.okta.poc.iot.tisensor.apidynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.stereotype.Repository;

import java.util.List;

@DynamoDBTable(tableName = "SensorPacket")
@Repository
public class SensorPacketModel extends SensorDataModel {

  //  @Id
  //  @DynamoDBIgnore
  //  @JsonIgnore
    String id;

    String userID;
    String deviceID;
    List<SensorDataModel> sensorDataModelList;
    long packetArrivalTime;


    @DynamoDBAttribute
    public Long getPacketArrivalTime() {
        return packetArrivalTime;
    }

    public void setPacketArrivalTime(Long packetArrivalTime) {
        this.packetArrivalTime = packetArrivalTime;
    }


    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public SensorPacketModel() {
    }

    public SensorPacketModel(String userID, String deviceID){
        this.userID = userID;
        this.deviceID = deviceID;
    }

    @DynamoDBAttribute
    public List<SensorDataModel> getSensorDataModelList(){ return sensorDataModelList;}
    public void setSensorDataModelList(List<SensorDataModel> sensorDataModelList) {
        this.sensorDataModelList = sensorDataModelList;
    }


    @DynamoDBAttribute
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @DynamoDBAttribute
    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
