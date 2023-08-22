package com.flySdk.model.Request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PhoneNumRequest implements Serializable {
    private String phoneNum;
}
