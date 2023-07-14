package com.fly.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.model.entity.UserInterfaceInfo;
import com.fly.service.UserInterfaceInfoService;
import com.fly.mapper.UserInterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2023-07-13 18:17:14
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

}




