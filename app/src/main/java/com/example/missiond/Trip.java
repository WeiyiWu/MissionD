package com.example.missiond;

import java.io.Serializable;

public class Trip implements Serializable {
    private String location;
    private String destination;
    //或者是其他什么type的 周总自己看着改
    private Integer distance;

    Trip(String location, String destination,Integer distance){
        this.location = location;
        this.destination = destination;
        this.distance = distance;
    }

    String getLocationName(){return this.location;}

    String getDestination(){return  this.destination;}

    Integer getDistance(){return  this.distance;}


    void setTrip(String location, String destination, Integer distance){
        this.location = location;
        this.destination = destination;
        this.distance = distance;
    }
}
