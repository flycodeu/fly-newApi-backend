package com.flyinterface.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ErrorCode;
import com.flyCommon.common.ResultUtils;
import com.flyCommon.exception.BusinessException;
import com.flyinterface.entity.PhoneAddress;
import com.flyinterface.entity.Request.PhoneNumRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取手机号地址
 */
@RestController
@RequestMapping( "/phoneAddress" )
public class PhoneAddressController {

    @PostMapping( "/zhishu" )
    public BaseResponse<PhoneAddress> phoneAddress(@RequestBody PhoneNumRequest phoneNumRequest) {
        if (phoneNumRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String phoneNum = phoneNumRequest.getPhoneNum();
        String url = "https://api.oioweb.cn/api/common/teladress";
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phoneNum);
        String res = HttpUtil.post(url, map);
        JSONObject jsonObject = new JSONObject(res);
        String code = jsonObject.getStr("code");
        if (!code.equals("200")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, jsonObject.getStr("msg"));
        }
        JSONObject data = jsonObject.getJSONObject("result");
        String name = data.getStr("name");
        String prov = data.getStr("prov");
        String city = data.getStr("city");
        String cityCode = data.getStr("cityCode");
        PhoneAddress phoneAddress = new PhoneAddress();
        phoneAddress.setName(name);
        phoneAddress.setProv(prov);
        phoneAddress.setCity(city);
        phoneAddress.setCityCode(cityCode);

        return ResultUtils.success(phoneAddress);
    }

}
