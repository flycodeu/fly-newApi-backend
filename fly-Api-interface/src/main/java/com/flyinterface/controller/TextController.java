package com.flyinterface.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ResultUtils;
import com.flyinterface.entity.FamousSayings;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping( "/text" )
@RestController
public class TextController {

    /**
     * 每日一言
     *
     * @return
     */
    @GetMapping( "/day/sayings" )
    public BaseResponse<String> getDaySayings() {
        String json = HttpUtil.get("https://api.gmit.vip/Api/YiYan?format=json");
        JSONObject jsonObject = new JSONObject(json);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        String sayingText = dataObject.getStr("text");
        return ResultUtils.success(sayingText);
    }

    /**
     * 返回名人名言
     * @return
     */
    @GetMapping( "/famous/sayings" )
    public BaseResponse<FamousSayings> getFamousSayings() {
        String json = HttpUtil.get("https://api.xygeng.cn/one");
        JSONObject jsonObject = new JSONObject(json);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        String name = dataObject.getStr("name");
        String content = dataObject.getStr("content");
        FamousSayings famousSayings = new FamousSayings();
        famousSayings.setAuthorName(name);
        famousSayings.setSayingContent(content);
        return ResultUtils.success(famousSayings);
    }
}
