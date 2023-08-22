package com.fly.mapper;

import com.flyCommon.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author admin
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-07-13 18:24:51
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT MONTH(createTime) AS month, COUNT(*) AS count " +
            "FROM user " +
            "WHERE createTime >= #{startDate} AND createTime < #{endDate} " +
            "GROUP BY MONTH(createTime) " +
            "ORDER BY MONTH(createTime)")
    List<Map<String, Object>> countUsersByMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);
}




