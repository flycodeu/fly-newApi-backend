package com.flyinterface.entity.Request;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeatherRequest implements Serializable {
    private String location;
}
