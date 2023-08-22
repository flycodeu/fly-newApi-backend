package com.flySdk.model;

import lombok.Data;

@Data
public class PhoneAddress {
    // 手机号运营商
    private String name;
    // 手机号省份
    private String prov;
    // 手机号城市
    private String city;
    // 手机号城市编码
    private String cityCode;
}
