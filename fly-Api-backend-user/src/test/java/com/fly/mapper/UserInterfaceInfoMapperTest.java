package com.fly.mapper;

import com.flyCommon.model.vo.InterfaceInoTimeVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserInterfaceInfoMapperTest {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Test
    void listAllInvokeInterfaceInfo() {
        List<InterfaceInoTimeVo> interfaceInoTimeVos = userInterfaceInfoMapper.listAllInvokeInterfaceInfo();

        Map<LocalDate, Map<Long, Integer>> map = interfaceInoTimeVos.stream()
                .collect(Collectors.groupingBy(
                        row -> row.getCallDay().toLocalDate(),
                        Collectors.toMap(
                                InterfaceInoTimeVo::getInterfaceInfoId,
                                InterfaceInoTimeVo::getAllInvokeCount,
                                (existingValue, newValue) -> newValue
                        )
                ));

        System.out.println(map);


    }
}