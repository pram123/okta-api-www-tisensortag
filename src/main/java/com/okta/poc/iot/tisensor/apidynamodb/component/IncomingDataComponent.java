package com.okta.poc.iot.tisensor.apidynamodb.component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.ParseException;
import com.okta.jwt.JoseException;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtHelper;
import com.okta.jwt.JwtVerifier;
import com.okta.poc.iot.tisensor.apidynamodb.model.*;
import com.okta.poc.iot.tisensor.apidynamodb.repository.AccelerometerRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.GyroscopeRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.SensorRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.UserModelRepository;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Component
@Service
public class IncomingDataComponent {

    private static Logger log = LoggerFactory.getLogger(IncomingDataComponent.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private AccelerometerRepository accelerometerRepository;

    @Autowired
    private GyroscopeRepository gyroscopeRepository;

    @Autowired
    private OutgoingDataComponent outgoingDataComponent;

    @Autowired
    private UserModelRepository userModelRepository;

    Buffer buf = new CircularFifoBuffer(4);


    static JwtVerifier jwtVerifier;

    public IncomingDataComponent(SensorRepository sensorRepository, AccelerometerRepository accelerometerRepository,
                                 GyroscopeRepository gyroscopeRepository, OutgoingDataComponent outgoingDataComponent,
                                 UserModelRepository userModelRepository)
            throws IOException, ParseException {
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        this.outgoingDataComponent = outgoingDataComponent;
        this.sensorRepository = sensorRepository;
        this.accelerometerRepository = accelerometerRepository;
        this.gyroscopeRepository = gyroscopeRepository;
        this.userModelRepository = userModelRepository;

        jwtVerifier = new JwtHelper()
                .setIssuerUrl("https://prdemo.oktapreview.com/oauth2/ausca4x6gjdoZn9u60h7")
                .setAudience("TISensorAPI")
                .build();
    }

    /* this function probably needs to be broken down since it does a host of things
        1. Validates the JWT token; if valid parse the rest of msg; else drop
        2. Break up the json packet into it's respective sensors
        3. Save the broken up data into a outgoing packet by calling the outgoing component
     */
    @Transactional
    public ResponseModel saveIncoming(JsonNode incomingPacket) {
        ResponseModel responseModel = new ResponseModel();
        List<SensorDataModel> sensorDataModelList = new ArrayList<>();
        UserModel tmpUserModel = new UserModel();
        GyroscopeDataModel gyro = null;
        AccelerometerDataModel accData = null;
        SensorDataModel savedSensorDataModel = null;

        try {
            /* lets update the userModel for tracking purposes */
           // UserModel userModel = new UserModel();

            SensorPacketModel sensorPacketModel = new SensorPacketModel();
            Map<String, Object> claims = this.getClaims(incomingPacket.get("accessToken").asText());
            String userID = claims.get("uid").toString();
            String deviceID = incomingPacket.get("deviceID").asText();
            //userModel.setUserID(userID);
            //userModel.setDeviceID(deviceID);
            //userModel.setCheckinTime(System.currentTimeMillis());

           // userModelRepository.save(userModel);
//           sharedMetricsComponent.setDeviceMoniterd(deviceID);

            log.info("Got info  deviceID: {} from userID {}", deviceID, userID);
            log.info("Incoming packet: {}", incomingPacket);
            sensorPacketModel.setDeviceID(deviceID);
            sensorPacketModel.setPacketArrivalTime(System.currentTimeMillis());
            sensorPacketModel.setUserID(userID);
            //SensorPacketModel aNode = objectMapper.readValue(incomingPacket.get("sensorData").toString(), SensorPacketModel.class);
            //Iterable<JsonNode> asd = incomingPacket.get("sensorData");
            JsonNode aNode = incomingPacket.get("sensorData");
            //for(JsonNode aNode : asd) {
            log.debug("ANode = {}", aNode);

               /* Save the environemntal data */
            long dataCaptureTime = aNode.get("dataCaptureTime").asLong();

            savedSensorDataModel = new SensorDataModel();
            savedSensorDataModel.setBarInfo(aNode.get("barInfo").asDouble());
            savedSensorDataModel.setTempInfo(aNode.get("tempInfo").asDouble());
            savedSensorDataModel.setDataCaptureTime(dataCaptureTime);
            log.debug("Saving sensorModel:", sensorPacketModel);
            //  sharedMetricsComponent.setSavedSensorDataModel(savedSensorDataModel);

            XYZModel data = new XYZModel();


               /* Accelerometer data */
            String newString = aNode.get("accInfo").asText();
            data = objectMapper.readValue(newString, XYZModel.class);
            accData = saveAccelerometerData(userID, deviceID, dataCaptureTime, data);


               /* Save the Gyroscope data */
            newString = aNode.get("gyroInfo").asText();
            log.debug("gyroInfo:{}", newString);
            data = null;
            data = objectMapper.readValue(newString, XYZModel.class);
            gyro = saveGyroscopeData(userID, deviceID, dataCaptureTime, data);

               /* newString = aNode.get("magInfo").asText();//.replaceAll("[^a-zA-Z0-9.-: \"]", "");;//.replaceAll("\"","");
               data = objectMapper.readValue(newString, XYZModel.class);
               //log.debug("AccInfo: {}, newString {} data{}", aNode.get("accInfo"), newString,data);
               savedSensorDataModel.setMagInfo(data);

                newString = aNode.get("gyroInfo").asText();//.replaceAll("[^a-zA-Z0-9.-: \"]", "");;//.replaceAll("\"","");
               data = objectMapper.readValue(newString, XYZModel.class);
               //log.debug("AccInfo: {}, newString {} data{}", aNode.get("accInfo"), newString,data);
               savedSensorDataModel.setGyroInfo(data);
               sensorDataModelList.add(savedSensorDataModel);
               */
            // }

            /* save info to database */
            sensorPacketModel.setSensorDataModelList(sensorDataModelList);
            this.sensorRepository.save(sensorPacketModel);

            /* we will send the last value to the real-time chart engine */
            tmpUserModel.setDeviceID(deviceID);
            tmpUserModel.setUserID(userID);
           /* lets save the outgoing model */
            HashMap<String, Object> sensorInfoToSend = new HashMap<>();
            sensorInfoToSend.put("accData", accData.getAccData());
            sensorInfoToSend.put("gyroData", gyro.getGyroData());
            sensorInfoToSend.put("envData", savedSensorDataModel);
            log.debug("Saving {} for {}", sensorInfoToSend, tmpUserModel);
            outgoingDataComponent.saveMessageFor(tmpUserModel, sensorInfoToSend);


            responseModel.responseShortMsg = "ok";
            responseModel.responseCode = HttpStatus.OK;
            //log.info("# events in database:{}", sensorRepository.count() );
            //this.sensorRepository.save(savedSensorDataModel);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.responseCode = HttpStatus.NOT_ACCEPTABLE;
            responseModel.responseShortMsg = e.getMessage();
        }

        return responseModel;
    }

    private GyroscopeDataModel saveGyroscopeData(String userID, String deviceID, long dataCaptureTime, XYZModel data) {
        GyroscopeDataModel gyroscopeDataModel = new GyroscopeDataModel();
        gyroscopeDataModel.setGyroData(data);
        gyroscopeDataModel.setDataCaptureTime(dataCaptureTime);
        gyroscopeDataModel.setDeviceID(deviceID);
        gyroscopeDataModel.setUserID(userID);
        log.debug("gyroscopeDataModel: {}", gyroscopeDataModel.toString());
        gyroscopeRepository.save(gyroscopeDataModel);   // save to database
        return gyroscopeDataModel;
    }

    private AccelerometerDataModel saveAccelerometerData(String userID, String deviceID, long dataCaptureTime, XYZModel data) {
        AccelerometerDataModel accelerometerDataModel = new AccelerometerDataModel();
        accelerometerDataModel.setAccData(data);
        accelerometerDataModel.setDataCaptureTime(dataCaptureTime);
        accelerometerDataModel.setDeviceID(deviceID);
        accelerometerDataModel.setUserID(userID);
        log.debug("AccInfo: {}", accelerometerDataModel);
        accelerometerRepository.save(accelerometerDataModel);  // save to database
        return accelerometerDataModel;

    }

    private Map<String, Object> getClaims(String accessToken) throws JoseException {
        Jwt jwt = jwtVerifier.decodeAccessToken(accessToken);
        return jwt.getClaims();
    }
}
