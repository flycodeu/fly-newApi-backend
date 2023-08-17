package com.flyCommon.model.vo;

import com.flyCommon.model.entity.InterfaceInfoNew;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
public class InterfaceInfoVo implements Serializable {
    private Long interfaceInfoId;
    private String interfaceInfoName;
    private Integer allInvokeNum;
}
