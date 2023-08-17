package com.fly.mapper;

import com.flyCommon.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyCommon.model.vo.InterfaceInfoVo;
import com.flyCommon.model.vo.InterfaceInoTimeVo;

import java.util.List;

/**
 * @author admin
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
 * @createDate 2023-07-13 18:17:14
 * @Entity generator.domain.UserInterfaceInfo
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    // select interfaceInfoId,
    //     sum(totalNum - leftNum) as allInvokeNum
    //from user_interface_info
    //group by interfaceInfoId
    //order by allInvokeNum desc limit 3;

    /**
     * 获取前top个数据
     * @param limit
     * @return
     */
    List<InterfaceInfoVo> listTopInvokeInterfaceInfo(int limit);

    /**
     * 获取所有数据
     * @return
     */
    List<InterfaceInoTimeVo> listAllInvokeInterfaceInfo();
}




