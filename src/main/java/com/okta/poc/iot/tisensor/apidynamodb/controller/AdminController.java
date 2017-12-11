package com.okta.poc.iot.tisensor.apidynamodb.controller;

import com.okta.poc.iot.tisensor.apidynamodb.model.ProductInfo;
import com.okta.poc.iot.tisensor.apidynamodb.repository.ProductInfoRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.SensorRepository;
import com.okta.poc.iot.tisensor.apidynamodb.repository.UserModelRepository;
import com.okta.poc.iot.tisensor.apidynamodb.service.AdminService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/rest/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Autowired
    UserModelRepository userInfoRepository;

    @RequestMapping(value = "/user/products", method = RequestMethod.GET,  produces = "application/json")
    public List<ProductInfo> getProducts(){
       return adminService.drumpProduct();
    }



    @RequestMapping(value = "/user/map", method = RequestMethod.GET, produces = "application/json")
   public Set<JSONObject> getUsers(){
       return adminService.getUniqueDevicesAndUsers();
    }


}
