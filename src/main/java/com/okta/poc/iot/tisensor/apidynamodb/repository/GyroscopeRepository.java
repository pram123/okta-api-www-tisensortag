package com.okta.poc.iot.tisensor.apidynamodb.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.okta.poc.iot.tisensor.apidynamodb.model.GyroscopeDataModel;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@DynamoDBTable(tableName = "GyroscopeData")

@EnableScan
public interface GyroscopeRepository extends CrudRepository<GyroscopeDataModel, String> {


    List<GyroscopeDataModel> findAllByDeviceID(String deviceID);
    List<GyroscopeDataModel> findAllByIdAfter(String id);
    List<GyroscopeDataModel> findById(String id);
    List<GyroscopeDataModel> findAllByUserIDAndAndDeviceIDAndDataCaptureTimeLessThanEqual(String userID, String deviceID,
                                                                                        long timeInMs);

    //    List<GyroscopeDataModel> findTop1ByUserIDAndDeviceIDAndDataCaptureTimeGreaterThanOrderByDataCaptureTimeDesc(String userID, String deviceID,

}
