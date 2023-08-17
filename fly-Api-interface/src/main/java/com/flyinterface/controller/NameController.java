package com.flyinterface.controller;


import com.flySdk.Utils.SignUtils;
import com.flySdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping( "/name" )
@RestController
public class NameController {

    @GetMapping( "/" )
    public String getNameByGet(String name, HttpServletRequest request) {
        System.out.println(request.getHeader("fly"));
        return "get your name=>" + name;
    }

    @PostMapping( "/post" )
    public String getNameByPost(@RequestParam String name) {
        return "Post your name=>" + name;
    }


    @PostMapping( "/user" )
    public String getNameByPostJson(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//
//        // 1. todo 使用数据库查询时候是有这个用户
//        if (!accessKey.equals("5a8bd83a0019aea1b4caa846bac07426")) {
//            throw new RuntimeException("accessKeyError");
//        }
//
//        //  2. todo 随机数，采用hashmap或者redis进行存储
//        if (Long.parseLong(nonce) > 100000) {
//            throw new RuntimeException("nonceError");
//        }
//
//        // 3. todo 校验时间不能超过5min
//        long time = System.currentTimeMillis() / 1000 - Long.parseLong(timestamp);
//        if (time / 1000 > 300L) {
//            throw new RuntimeException("error");
//        }
//        // 4. todo 校验body,实际请求是从数据库获取的
//        String signServer = SignUtils.genSign(body, "b55db76db7b87adac5df9215dc0afcb8");
//        if (!sign.equals(signServer)) {
//            throw new RuntimeException("signError");
//        }

        String result = "Post your name=>" + user.getName();
        // todo 调用次数+1

        return result;
    }
}
