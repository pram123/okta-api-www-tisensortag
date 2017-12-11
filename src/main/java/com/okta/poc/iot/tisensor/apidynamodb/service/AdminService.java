package com.okta.poc.iot.tisensor.apidynamodb.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.okta.poc.iot.tisensor.apidynamodb.component.IncomingDataComponent;
import com.okta.poc.iot.tisensor.apidynamodb.model.ProductInfo;
import com.okta.poc.iot.tisensor.apidynamodb.model.UserModel;
import com.okta.poc.iot.tisensor.apidynamodb.repository.ProductInfoRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.SensorRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.UserModelRepository;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Autowired
    UserModelRepository userModelRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    MetricsService metricsService;


    //TODO: CHANGE!!
    BasicAWSCredentials awsCreds = new BasicAWSCredentials("access_key_id", "secret_key_id");

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration("http://localhost:8000","")) //.EndpointConfiguration("http://localhost:8000", "us-west-2",))
            .build();
    DynamoDB dynamoDB = new DynamoDB(client);



    private static Logger log = LoggerFactory.getLogger(IncomingDataComponent.class);

    public List<ProductInfo> drumpProduct(){
        Iterable<ProductInfo> tmp = productInfoRepository.findAll();
        List<ProductInfo> list = new ArrayList<>();
        tmp.forEach(list::add);
        return list;
    }

    public void getOtherMetrics(){

    }

     /* lets get the  of unique users and devices */
    //TODO: use dynamoDBs GSI/LSI approach
    public Set<JSONObject> getUniqueDevicesAndUsers(){
        Iterable<UserModel> tmp = userModelRepository.findAll();
        HashSet<JSONObject> list = new HashSet();
        //tmp.forEach(list::add);
        for (Iterator i = tmp.iterator(); i.hasNext();) {
            UserModel um = (UserModel) i.next();
            JSONObject json = new JSONObject();
            json.put("userID",um.getUserID());
            json.put("deviceID",um.getDeviceID());
            list.add(json);
        }
        return list;
    }



}
