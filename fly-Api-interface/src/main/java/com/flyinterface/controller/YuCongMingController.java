package com.flyinterface.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ErrorCode;
import com.flyCommon.common.ResultUtils;
import com.flyCommon.exception.BusinessException;
import com.flyinterface.Key;
import com.flyinterface.config.YuAiConfig;
import com.flyinterface.entity.Request.YuCongMingAiRequest;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping( "/ai" )
public class YuCongMingController {
    @Resource
    private YuAiConfig yuAiConfig;

    /**
     * 鱼聪明AI
     *
     * @return
     */
    @PostMapping( "/yucchat" )
    public BaseResponse<String> yucAIChat(@RequestBody YuCongMingAiRequest yuCongMingAiRequest) {
        if (yuCongMingAiRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String msg = yuCongMingAiRequest.getMsg();
        String result = yuAiConfig.doChart(1651468516836098050L, msg);
        return ResultUtils.success(result);
    }


    @PostMapping( "/minimaxAi" )
    public BaseResponse<String> miniChat(@RequestBody YuCongMingAiRequest yuCongMingAiRequest) {
        if (yuCongMingAiRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String group_id = Key.mini_max_group_id;
        String api_key = Key.mini_max_api_key;
        String msg = yuCongMingAiRequest.getMsg();
        String url = "https://api.minimax.chat/v1/text/chatcompletion_pro?GroupId=" + group_id;

        String requestBody = "{" +
                "\"bot_setting\": [{" +
                "\"bot_name\": \"MM智能助理\"," +
                "\"content\": \"MM智能助理是一款由MiniMax自研的，没有调用其他产品的接口的大型语言模型。MiniMax是一家中国科技公司，一直致力于进行大模型相关的研究。\"" +
                "}]," +
                "\"messages\": [{" +
                "\"sender_type\": \"USER\"," +
                "\"sender_name\": \"小明\"," +
                "\"text\": \"" + msg + "\"" + // 将msg的值插入到"text"字段
                "}]," +
                "\"reply_constraints\": {" +
                "\"sender_type\": \"BOT\"," +
                "\"sender_name\": \"MM智能助理\"" +
                "}," +
                "\"model\": \"abab5.5-chat\"," +
                "\"tokens_to_generate\": 1034," +
                "\"temperature\": 0.01," +
                "\"top_p\": 0.95" +
                "}";

        // 设置请求头
        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + api_key)
                .charset(CharsetUtil.UTF_8)
                .body(requestBody);

        // 发送POST请求
        HttpResponse response = request.execute();

        // 打印响应状态码和内容
//        System.out.println("Status Code: " + response.getStatus());
        System.out.println("Response Body: " + response.body());

        String res = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            // 提取"reply"字段的值
            res= jsonNode.get("reply").asText();
            // 打印提取的reply
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultUtils.success(res);
    }

}
