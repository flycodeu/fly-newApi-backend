package com.flyCommon.model.request.UserInterface;

import com.baomidou.mybatisplus.annotation.TableField;
import com.flyCommon.model.constant.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    private Long id;
    /**
     * 调用者id
     */
    private Long userId;

    /**
     * 调用接口id
     */
    private Long interfaceInfoId;

    /**
     * 总数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    private Integer status;

    @TableField( exist = false )
    private static final long serialVersionUID = 1L;
}