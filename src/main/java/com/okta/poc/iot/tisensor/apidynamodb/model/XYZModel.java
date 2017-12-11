package com.okta.poc.iot.tisensor.apidynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument

public class XYZModel {
    double x,y,z;

    public void XYZModel(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "XYZModel{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
