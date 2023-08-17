package com.flyCommon.model.vo;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class InterfaceInoTimeVo {
    private Long interfaceInfoId;
    private Integer allInvokeCount;
    private LocalDateTime callDay;
}
