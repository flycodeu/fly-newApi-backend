package com.flySdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.flySdk.model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.flySdk.Utils.SignUtils.genSign;

/**
 * 调用第三方接口客户端
 *
 * @author fly
 */
public class FlyApiClient {
    public static final String HTTP_URL = "http://localhost:7550/api";
    private final String accessKey;
    private final String secretKey;

    public FlyApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    // get
    public String getNameByGet(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        String result = HttpUtil.get(HTTP_URL + "/name/", map);
        System.out.println("get=>" + result);
        System.out.println("");
        return result;
    }

    // post传参
    public String getNameByPost(String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        String result = HttpUtil.post(HTTP_URL + "/name/", map);
        System.out.println("post_param=>" + result);
        System.out.println("");
        return result;
    }

    public Map<String, String> getHeaders(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        // 不能直接发送给后端
//        map.put("secretKey", secretKey);
        map.put("nonce", RandomUtil.randomNumbers(5));
        body = URLUtil.encode(body, CharsetUtil.CHARSET_UTF_8);
        map.put("body", body);
//        try {
//            map.put("body", URLEncoder.encode(body, StandardCharsets.UTF_8.name()));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", genSign(body,secretKey));
        return map;
    }


    // post传对象
    public String getNameByPostJson(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest
                .post(HTTP_URL + "/name/user")
                .addHeaders(getHeaders(json))
                .body(json)
                .execute();
        System.out.println("json_user_status=>" + response.getStatus());
        String result = response.body();
        System.out.println("json=>" + result);
        System.out.println("");
        return result;
    }
}
