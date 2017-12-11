package com.okta.poc.iot.tisensor.apidynamodb.controller;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Calendar;

@Controller
public class UserControllerSocket {

    @MessageMapping("/random")
    @SendTo("/topic/number")
    public JSONArray guestbook(String message) throws InterruptedException {
        Thread.sleep(1000); // simulated delay

            JSONObject item = new JSONObject();

            JSONArray array = new JSONArray();
            item.put("currTime", Calendar.getInstance().getTimeInMillis());
            item.put("randomNum", Math.random() * 50 + 1);
            array.add(item);
            return array;
    }
}
