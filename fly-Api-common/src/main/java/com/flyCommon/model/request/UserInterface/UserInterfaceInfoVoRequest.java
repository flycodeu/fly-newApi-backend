package com.flyCommon.model.request.UserInterface;

import com.flyCommon.model.constant.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode( callSuper = true )
@Data
public class UserInterfaceInfoVoRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -511937282563148306L;
    private Long id;
    private Long userId;
    private Long interfaceInfoId;
    private String userName;
    private String interfaceInfoName;
    private Integer leftCount;
    private Integer totalCount;
    private Integer status;
}
