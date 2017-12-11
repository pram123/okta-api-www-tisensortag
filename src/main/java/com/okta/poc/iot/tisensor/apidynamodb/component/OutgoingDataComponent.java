package com.okta.poc.iot.tisensor.apidynamodb.component;

/*
We will use this component to send the metrics and other info that's needed
 */

import com.okta.poc.iot.tisensor.apidynamodb.model.UserModel;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OutgoingDataComponent {

    // this is the max number of elements we'll store in the queue
    static  final int MAX_BUFFER_SIZE = 4;


    static HashMap<String,CircularFifoBuffer > outgoingMsg = new HashMap();
    private static Logger log = LoggerFactory.getLogger(OutgoingDataComponent.class);

    public void OutgoingDataComponent(){

    }

    public void saveMessageFor(UserModel userModel, Object sensorPacketModel){

        userModel.setId(null);  // we won't have the id when requesting
        log.debug("Need to save msg for: {}", userModel.hashCode());
        CircularFifoBuffer buf = new CircularFifoBuffer(MAX_BUFFER_SIZE);
        buf = outgoingMsg.get(userModel.getUserID() + userModel.getDeviceID());
        if (buf == null) {
            buf = new CircularFifoBuffer(MAX_BUFFER_SIZE);
        }
        log.debug("{} msgs in buffer for {} ", buf.size(), userModel);
        buf.add(sensorPacketModel);
        outgoingMsg.put(userModel.getUserID()+userModel.getDeviceID(),buf);
    }

    public Object getMessageFor(UserModel userModel){
        userModel.setId(null);
        try {
        log.debug("Need to get msg for: {}", userModel.hashCode());
        if (outgoingMsg.containsKey(userModel.getUserID()+userModel.getDeviceID())) {
            log.debug("Contains key for {}", userModel);
            CircularFifoBuffer tmpBuf = outgoingMsg.get(userModel.getUserID() + userModel.getDeviceID());
            Object f = tmpBuf.remove();
            return f;
            }
        } catch (Exception e){
            log.debug("Oops - no new msg for {} ", userModel);
            return  null;
        }
        return  null;
    }

}
