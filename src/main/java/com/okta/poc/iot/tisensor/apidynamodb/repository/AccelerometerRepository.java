package com.okta.poc.iot.tisensor.apidynamodb.repository;

import com.okta.poc.iot.tisensor.apidynamodb.model.AccelerometerDataModel;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface AccelerometerRepository extends CrudRepository<AccelerometerDataModel, Integer> {


    List<AccelerometerDataModel> findAllByDeviceID(String deviceID);
    List<AccelerometerDataModel> findAllByIdAfter(Integer id);
    List<AccelerometerDataModel> findById(Integer id);
    List<AccelerometerDataModel> getAllByUserIDAndDeviceIDAndDataCaptureTimeGreaterThan(String userID, String deviceID,
                                                                                         long timeInMs);
    List<AccelerometerDataModel> findAllByDeviceIDAndUserID(String deviceID, String userID);
}
