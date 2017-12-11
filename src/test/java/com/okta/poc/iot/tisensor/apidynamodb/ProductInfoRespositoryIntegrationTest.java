package com.okta.poc.iot.tisensor.apidynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.okta.poc.iot.tisensor.apidynamodb.model.*;
import com.okta.poc.iot.tisensor.apidynamodb.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OktaAPIWWWTiSensorTag.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:8000/",
        "amazon.aws.accesskey=test1",
        "amazon.aws.secretkey=test231" })
public class ProductInfoRespositoryIntegrationTest {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    ProductInfoRepository repository;

    @Autowired
    UserModelRepository userModelRepository;

    @Autowired
    AccelerometerRepository accelerometerRepository;
    @Autowired
    GyroscopeRepository gyroscopeRepository;

    @Autowired
    SensorRepository sensorRepository;

    private static final String EXPECTED_COST = "20";
    private static final String EXPECTED_PRICE = "50";

    @Before
    public void setup() throws Exception {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        try {amazonDynamoDB.deleteTable("UserModel"); } catch (Exception e) {} finally {}; // amazonDynamoDB.deleteTable("ProductInfo");
        try {    amazonDynamoDB.deleteTable("SensorPacket"); } catch (Exception e) {} finally {};
        try {    amazonDynamoDB.deleteTable("AccelerometerData"); } catch (Exception e) {} finally {};
        try {    amazonDynamoDB.deleteTable("GyroscopeData"); } catch (Exception e) {} finally {};


        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(SensorPacketModel.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);

         tableRequest = dynamoDBMapper
                .generateCreateTableRequest(UserModel.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        //tableRequest.getGlobalSecondaryIndexes()
          //      .get(0).setProvisionedThroughput(new ProvisionedThroughput(10l, 10l));

        amazonDynamoDB.createTable(tableRequest);


         tableRequest = dynamoDBMapper
                .generateCreateTableRequest(GyroscopeDataModel.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);

         tableRequest = dynamoDBMapper
                .generateCreateTableRequest(AccelerometerDataModel.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);

         /*tableRequest = dynamoDBMapper
                .generateCreateTableRequest(ProductInfo.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);
*/
    }

    //@Test
    public void sampleTestCase() {
        ProductInfo dave = new ProductInfo(EXPECTED_COST, EXPECTED_PRICE);
        repository.save(dave);

        List<ProductInfo> result
                = (List<ProductInfo>) repository.findAll();

        assertTrue("Not empty", result.size() > 0);
        assertTrue("Contains item with expected cost",
                result.get(0).getCost().equals(EXPECTED_COST));
    }


    @Test
    public void sampleUserTestCase(){
        UserModel aUser = new UserModel("12345", "789", System.currentTimeMillis() % 1000,null);
        userModelRepository.save(aUser);
        List<UserModel> userResult =
                (List<UserModel>) userModelRepository.findAll();
        assertTrue("Not empty", userResult.size() > 0);
        assertTrue("Contains item with expected deviceID",
                userResult.get(0).getDeviceID().equals("789"));

    }


    @Test
    public void testAccelerometerRepo(){
        AccelerometerDataModel accelerometerDataModel = new AccelerometerDataModel("123456","789");
        accelerometerRepository.save(accelerometerDataModel);
        List<AccelerometerDataModel> userResult =
                (List<AccelerometerDataModel>) accelerometerRepository.findAll();
        assertTrue("Not empty", userResult.size() > 0);
        assertTrue("Contains item with expected deviceID",
                userResult.get(0).getDeviceID().equals("789"));
    }

    @Test
    public void testGyrorRepo(){
        GyroscopeDataModel gyroscopeDataModel = new GyroscopeDataModel("123456","789");
        gyroscopeRepository.save(gyroscopeDataModel);
        List<GyroscopeDataModel> userResult =
                (List<GyroscopeDataModel>) gyroscopeRepository.findAll();
        assertTrue("Not empty", userResult.size() > 0);
        assertTrue("Contains item with expected deviceID",
                userResult.get(0).getDeviceID().equals("789"));
    }

    @Test
    public void testSensorRepo(){
        SensorPacketModel gyroscopeDataModel = new SensorPacketModel("123456","789");
        sensorRepository.save(gyroscopeDataModel);
        List<SensorPacketModel> userResult =
                (List<SensorPacketModel>) sensorRepository.findAll();
        assertTrue("Not empty", userResult.size() > 0);
        assertTrue("Contains item with expected deviceID",
                userResult.get(0).getDeviceID().equals("789"));
    }
}