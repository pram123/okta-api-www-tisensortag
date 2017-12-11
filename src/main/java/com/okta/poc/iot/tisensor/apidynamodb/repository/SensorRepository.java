package com.okta.poc.iot.tisensor.apidynamodb.repository;

import com.okta.poc.iot.tisensor.apidynamodb.model.SensorDataModel;
import com.okta.poc.iot.tisensor.apidynamodb.model.SensorPacketModel;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.List;

@EnableScan
public interface  SensorRepository extends PagingAndSortingRepository<SensorPacketModel, String> {

    ArrayList<SensorDataModel>  findFirst3ByOrderByDeviceIDDescUserID(String deviceID, String userID); //findFirst5ByOrderByPublicationDateDesc();
    public void deleteAllByUserIDMatches(String userID);
    public  void deleteAllByDeviceIDAndUserID(String deviceID, String userID);
    public ArrayList<String> findDistinctByDeviceID(String userID);
    public ArrayList<SensorPacketModel> findAllByUserID(String userID);
    public  SensorPacketModel findAllByDeviceID(String deviceID);

    List<SensorDataModel> getAllByUserIDAndDeviceIDAndDataCaptureTimeGreaterThan(String userId, String deviceId,
                                                                                 long dataCaptureTime);

}
