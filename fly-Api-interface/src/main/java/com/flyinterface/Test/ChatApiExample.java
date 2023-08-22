package com.flyinterface.Test;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatApiExample {
    public static void main(String[] args) {
        String group_id = "1692538450973139";
        String api_key = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJOYW1lIjoiMTExIiwiU3ViamVjdElEIjoiMTY5MjUzODQ1MDQyMTc5MyIsIlBob25lIjoiIiwiR3JvdXBJRCI6IjE2OTI1Mzg0NTA5NzMxMzkiLCJQYWdlTmFtZSI6IiIsIk1haWwiOiIxNzg1NjEzMDQ4QHFxLmNvbSIsIkNyZWF0ZVRpbWUiOiIyMDIzLTA4LTIwIDIxOjM2OjUwIiwiaXNzIjoibWluaW1heCJ9.mmTZylAOFf7KqHmK40p5GiGRGCLS2ei_DxEs8NtbSf-SyMHPPpC2KEXwrlTY0xGzNbmStRefAT_AZkLD5O8AYw67DIWg9AiUCHMq_BsZwSEuDjpj24R8g1hVPHe5BFWuoy1eHtIVTKIVFUz2hJhsHtqs2p230H12hkJd0hghCqUGBvsR-TURXeUBYHoOJlk9xFI6ZdH-Ozkjrf3rgfDC9M8AlC4ejXzKRTcrSt0Bxjr42kTkWSmC-wqINgxmugKUCR7BaXLwiDHQqYJRz1gsTSNmew8rgX0FPacF94-Nc7QmWFaqEwOJbWPQfxrOyIRDdosgJcDu98tRYGNEnq5P1Q"; // 替换为你的API密钥

        String url = "https://api.minimax.chat/v1/text/chatcompletion_pro?GroupId=" + group_id;

        String requestBody = "{" +
                "\"bot_setting\": [{" +
                "\"bot_name\": \"MM智能助理\"," +
                "\"content\": \"MM智能助理是一款由MiniMax自研的，没有调用其他产品的接口的大型语言模型。MiniMax是一家中国科技公司，一直致力于进行大模型相关的研究。\"" +
                "}]," +
                "\"messages\": [{" +
                "\"sender_type\": \"USER\"," +
                "\"sender_name\": \"小明\"," +
                "\"text\": \"java是什么\"" +
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
        System.out.println("Status Code: " + response.getStatus());
        System.out.println("Response Body: " + response.body());



        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            // 提取"reply"字段的值
            String reply = jsonNode.get("reply").asText();

            // 打印提取的reply
            System.out.println("Reply: " + reply);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}