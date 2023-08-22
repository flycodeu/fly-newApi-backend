package com.flySdk.model;

import lombok.Data;

@Data
public class XinZhiWeather {
    // 地址
    private String location;
    // 温度
    private String temperature;
    // 天气
    private String weather;
    // 上次更新时间
    private String last_update;
}
