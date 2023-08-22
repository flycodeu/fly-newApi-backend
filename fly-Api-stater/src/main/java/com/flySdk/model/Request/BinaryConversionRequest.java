package com.flySdk.model.Request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BinaryConversionRequest implements Serializable {
    // 原数
    private Long originalNumber;
    // 目标进制
    private Long targetBinary;

    private static final long serialVersionUID = -928990601279847525L;
}
