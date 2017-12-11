package com.okta.poc.iot.tisensor.apidynamodb.repository;

import com.okta.poc.iot.tisensor.apidynamodb.model.UserModel;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@EnableScan
//@DynamoDBTable(tableName = "UserModel")
public interface UserModelRepository extends
        PagingAndSortingRepository<UserModel, String> {
    
    List<UserModel> findById(String id);
    //List<UserModel> findByUserID(String userID);
   // List<UserModel>  g();
}