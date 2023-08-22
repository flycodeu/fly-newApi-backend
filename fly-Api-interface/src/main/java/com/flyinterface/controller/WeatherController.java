package com.flyinterface.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ErrorCode;
import com.flyCommon.common.ResultUtils;
import com.flyCommon.exception.BusinessException;
import com.flyinterface.Key;
import com.flyinterface.entity.Request.WeatherRequest;
import com.flyinterface.entity.XinZhiWeather;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/weather" )
public class WeatherController {

    /**
     * 调用心知天气
     *
     * @return
     */
    @PostMapping( "/localtion" )
    public BaseResponse<XinZhiWeather> xinzhiWeather(@RequestBody WeatherRequest weatherRequest) {
        if (weatherRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String location = weatherRequest.getLocation();
        String url = "https://api.seniverse.com/v3/weather/now.json?key=" + Key.xinZhiAPIKey +
                "&location=" + location + "&language=zh-Hans&unit=c";

        String jsonString = HttpUtil.get(url); // Assuming you have an HttpUtil to fetch data
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray resultsArray = jsonObject.getJSONArray("results");

        if (resultsArray == null || resultsArray.size() <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "暂不支持该地区");
        }

        XinZhiWeather xinZhiWeather = new XinZhiWeather();
        for (int i = 0; i < resultsArray.size(); i++) {
            JSONObject resultsObject = resultsArray.getJSONObject(i);

            JSONObject locationObject = resultsObject.getJSONObject("location");
            String name = locationObject.getStr("name");

            JSONObject nowObject = resultsObject.getJSONObject("now");
            String weather = nowObject.getStr("text");
            String temperature = nowObject.getStr("temperature");

            String lastUpdate = resultsObject.getStr("last_update");

            //System.out.println(name + " " + weather + " " + temperature + " " + lastUpdate);

            xinZhiWeather.setLocation(name);
            xinZhiWeather.setTemperature(temperature);
            xinZhiWeather.setWeather(weather);
            xinZhiWeather.setLast_update(lastUpdate);
        }


        return ResultUtils.success(xinZhiWeather);
    }


    /**
     * 调用博天天气
     * @return
     */
    @GetMapping("/botian")
    public BaseResponse<String> botianWeather(){
        String url = "https://api.oioweb.cn/api/weather/GetWeather";
        String res = HttpUtil.get(url);
        return ResultUtils.success(res);
    }

}
