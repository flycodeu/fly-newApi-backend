package com.flySdk.model.Request;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeatherRequest implements Serializable {
    private String location;
}
