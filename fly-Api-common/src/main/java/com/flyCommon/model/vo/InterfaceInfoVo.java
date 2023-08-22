package com.flyCommon.model.vo;

import com.flyCommon.model.entity.InterfaceInfoNew;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode( callSuper = true )
@Data
public class InterfaceInfoVo extends InterfaceInfoNew implements Serializable {
    private static final long serialVersionUID = -2814059758963007103L;
    private Integer totalNum;
}
