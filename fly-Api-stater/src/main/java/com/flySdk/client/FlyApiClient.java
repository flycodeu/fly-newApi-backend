package com.flySdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.flySdk.model.Request.*;
import com.flySdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.flySdk.Utils.SignUtils.genSign;

/**
 * 调用第三方接口客户端
 *
 * @author fly
 */
public class FlyApiClient {
    private static final String HTTP_URL = "http://localhost:7550/api";
    private final String accessKey;
    private final String secretKey;

    public FlyApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    // get获取名字
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

    private Map<String, String> getHeaders(String body) {
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
        map.put("sign", genSign(body, secretKey));
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


    // get获取豆瓣热销书
    public String getDouBanFamousBook() {
        HttpResponse response = HttpRequest.get(HTTP_URL + "/book/douban")
                .addHeaders(getHeaders(""))
                .execute();
        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }


    /**
     * 获取豆瓣热门电影
     *
     * @return
     */
    public String getFilmByDouBan() {
        HttpResponse response = HttpRequest.get(HTTP_URL + "/film/douban")
                .addHeaders(getHeaders(""))
                .execute();

        if (response.isOk()) {
            return response.body();
        }

        return "fail";
    }


    /**
     * 返回名人名言
     *
     * @return
     */
    public String getFamousSayings() {
        HttpResponse response = HttpRequest.get(HTTP_URL + "/text/famous/sayings")
                .addHeaders(getHeaders(""))
                .execute();
        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }


    /**
     * 返回每日一言
     *
     * @return
     */
    public String getDaySayings() {
        HttpResponse response = HttpRequest.get(HTTP_URL + "/text/day/sayings")
                .addHeaders(getHeaders(""))
                .execute();

        if (response.isOk()) {
            return response.body();
        }

        return "fail";
    }


    /**
     * 根据手机号获取地址
     *
     * @return
     */
    public String getPhoneAddress(PhoneNumRequest phoneNumRequest) {
        String json = JSONUtil.toJsonStr(phoneNumRequest);
        HttpResponse response = HttpRequest
                .post(HTTP_URL + "/phoneAddress/zhishu")
                .addHeaders(getHeaders(json))
                .body(json)
                .execute();

        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }

    /**
     * 爬取bing随机指定图片
     *
     * @param randomPictureRequest
     * @return
     */
    public String getBingRandomPicture(RandomPictureRequest randomPictureRequest) {
        String json = JSONUtil.toJsonStr(randomPictureRequest);
        HttpResponse response = HttpRequest
                .post(HTTP_URL + "/picture/bing/randomPicture")
                .addHeaders(getHeaders(json))
                .body(json)
                .execute();
        return response.body();
    }

    /**
     * 获取简单天气
     *
     * @return
     */
    public String getSimpleWeather(WeatherRequest weatherRequest) {
        String json = JSONUtil.toJsonStr(weatherRequest);
        HttpResponse response = HttpRequest
                .post(HTTP_URL + "/weather/localtion")
                .addHeaders(getHeaders(json))
                .body(json)
                .execute();

        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }

    /**
     * 调用博天天气API
     *
     * @return
     */
    public String getAllWeather() {
        HttpResponse response = HttpRequest.get(HTTP_URL + "/weather/botian")
                .addHeaders(getHeaders(""))
                .execute();

        if (response.isOk()) {
            return response.body();
        }

        return "fail";
    }

    /**
     * 鱼聪明AI聊天
     *
     * @return
     */
    public String getYuCongMingAIChat(YuCongMingAiRequest yuCongMingAiRequest) {
        String json = JSONUtil.toJsonStr(yuCongMingAiRequest);
        HttpResponse response = HttpRequest
                .post(HTTP_URL + "/ai/yucchat")
                .addHeaders(getHeaders(json))
                .body(json)
                .execute();

        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }

    /**
     * miniAi 接口
     *
     * @param yuCongMingAiRequest
     * @return
     */
    public String getMiNiMaxAi(YuCongMingAiRequest yuCongMingAiRequest) {
        String json = JSONUtil.toJsonStr(yuCongMingAiRequest);
        HttpResponse response = HttpRequest
                .post(HTTP_URL + "/ai/minimaxAi")
                .addHeaders(getHeaders(json))
                .body(json)
                .execute();

        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }

    /**
     * 返回历史上的今天
     *
     * @return
     */
    public String getHistoryToday() {
        HttpResponse response = HttpRequest.get(HTTP_URL + "/text/historyToday")
                .addHeaders(getHeaders(""))
                .execute();

        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }

    /**
     * 获取每日英文
     *
     * @return
     */
    public String getOneDayEnglish() {
        HttpResponse response = HttpRequest.get(HTTP_URL + "/text/englishOneDay")
                .addHeaders(getHeaders(""))
                .execute();

        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }


    /**
     * 指定进制转换
     *
     * @param binaryConversionRequest
     * @return
     */
    public String binaryConversion(BinaryConversionRequest binaryConversionRequest) {
        String json = JSONUtil.toJsonStr(binaryConversionRequest);
        HttpResponse response = HttpRequest
                .post(HTTP_URL + "/binary/conversion")
                .addHeaders(getHeaders(json))
                .body(json)
                .execute();

        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }


    /**
     * 网易云热歌随机
     *
     * @return
     */
    public String wangyiyunRandomMusic() {
        HttpResponse response = HttpRequest.get(HTTP_URL + "/music/wangyiyun/rege")
                .addHeaders(getHeaders(""))
                .execute();

        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }


    /**
     * 快手热搜
     *
     * @return
     */
    public String getKuaiShouReSou() {
        HttpResponse response = HttpRequest.get(HTTP_URL + "/resou/kuaishou")
                .addHeaders(getHeaders(""))
                .execute();

        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }
}
