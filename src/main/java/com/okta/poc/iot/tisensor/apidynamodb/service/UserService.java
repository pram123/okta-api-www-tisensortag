package com.okta.poc.iot.tisensor.apidynamodb.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.poc.iot.tisensor.apidynamodb.component.OutgoingDataComponent;
import com.okta.poc.iot.tisensor.apidynamodb.model.*;
import com.okta.poc.iot.tisensor.apidynamodb.repository.AccelerometerRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.GyroscopeRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.SensorRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.UserModelRepository;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.ClientBuilder;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    AccelerometerRepository accelerometerRepository;

    @Autowired
    GyroscopeRepository gyroscopeRepository;

    @Autowired
    OutgoingDataComponent outgoingDataComponent;

    @Autowired
    MetricsService metricsService;

    @Autowired
    UserModel userModel;

    @Autowired
    private UserModelRepository userModelRepository;

    private static Logger log = LoggerFactory.getLogger(UserService.class);
    ObjectMapper mapper = new ObjectMapper();

    //TODO: CHANGE!!
    BasicAWSCredentials awsCreds = new BasicAWSCredentials("access_key_id", "secret_key_id");

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration("http://localhost:8000","")) //.EndpointConfiguration("http://localhost:8000", "us-west-2",))
            .build();
    DynamoDB dynamoDB = new DynamoDB(client);

    // lets get all the devices this user is registered for
    @Cacheable
    public ArrayList<String> getMyDevices(String userID){
       return sensorRepository.findDistinctByDeviceID(userID);
    }

    public ArrayList<SensorPacketModel> getAllMyInfo(String userId) {
        return  sensorRepository.findAllByUserID(userId);
    }

    public void  deleteUser(String userId) {
         sensorRepository.deleteAllByUserIDMatches(userId);
    }

    public void  deleteUserDevice(String userId, String deviceID) {
         sensorRepository.deleteAllByDeviceIDAndUserID(deviceID,userId);
    }

    public ArrayList<SensorDataModel> getLatest(String userId, String deviceID) {
       return sensorRepository.findFirst3ByOrderByDeviceIDDescUserID(deviceID,userId);
    }


    public void addDeviceID(String userId, String deviceId) {
        // lets talk to okta, get theuser info and add this deviceID to the users attribute
    }

    /* AccelerometerDataModel  */
    public List<AccelerometerDataModel> dumpAcc(String userID, String deviceID){
        return accelerometerRepository.findAllByDeviceID(deviceID);
    }
    public List<AccelerometerDataModel> getLatestAccData(String userID, String deviceID, String timeInMs){
        return accelerometerRepository.getAllByUserIDAndDeviceIDAndDataCaptureTimeGreaterThan(userID,deviceID,
                Long.parseLong(timeInMs));
    }


    /* Get the latest sensor data for this user and device combo - if anything exists */
    public Object getLatestSensorData(String userId, String deviceId) {
        UserModel  userModel = new UserModel();
        userModel.setDeviceID(deviceId);
        userModel.setUserID(userId);
        return outgoingDataComponent.getMessageFor(userModel);
    }

    @Cacheable
    public Object getLatestGyroDataSincetime(String userId, String deviceId, String timeInMs) {
        List<GyroscopeDataModel> gyroscopeDataModelList =new ArrayList<>();
        UserModel  userModel = new UserModel();
        userModel.setDeviceID(deviceId);
        userModel.setUserID(userId);
       return outgoingDataComponent.getMessageFor(userModel);
//        String sql = "SELECT * from GyroscopeData WHERE  dataCaptureTime > " + Long.parseLong(timeInMs));
        /*
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":dataCaptureTime", new AttributeValue().withN(String.valueOf(Long.parseLong(timeInMs))));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("dataCaptureTime > :dataCaptureTime").withExpressionAttributeValues(eav);

        List<GyroscopeDataModel> scanResult = mapper.scan(GyroscopeDataModel.class, scanExpression);

        for (GyroscopeDataModel book : scanResult) {
            log.debug(book.getGyroData().toString());
        }
*/
        //return null;
    }
    public List<GyroscopeDataModel> dumpGyro(String userID, String deviceID){
        return gyroscopeRepository.findAllByDeviceID(deviceID);
    }

    /* Use this function to save the sensor info to Okta for this user*/
    public User init(JsonNode init) throws JsonProcessingException {
        ClientBuilder builder = Clients.builder();
        Client client = builder.build();

        String userID  = init.get("userID").asText();
        String deviceID = init.get("deviceID").asText();
        User user = client.getUser(userID);
        UserProfile profile = user.getProfile();
        profile.put("deviceID",deviceID);
        user.setProfile(profile);
        user.update();
        log.debug("Updated userID {} with deviceID {}", userID,deviceID);
        ResponseModel rm = new ResponseModel();
        // lets save this info
        UserModel userModel = new UserModel();

        userModel.setUserID(userID);
        userModel.setDeviceID(deviceID);
        userModel.setCheckinTime(System.currentTimeMillis());
        userModel.setUserName(mapper.writeValueAsString(user));
        userModelRepository.save(userModel);

        return user;
    }


}
