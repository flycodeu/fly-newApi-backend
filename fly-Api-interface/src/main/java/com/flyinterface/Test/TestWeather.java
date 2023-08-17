package com.flyinterface.Test;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class TestWeather {

    public static void main(String[] args) {
        String url = "https://api.seniverse.com/v3/weather/now.json?key=SW0AkZ0B-fDGe85J5&location=" + "南通" + "&language=zh-Hans&unit=c";
        String res = HttpUtil.get(url);

        JSONObject jsonObject = new JSONObject(res);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        for (int i = 0; i < resultsArray.size(); i++) {
            JSONObject resultsObject = resultsArray.getJSONObject(i);

            JSONObject locationObject = resultsObject.getJSONObject("location");
            String name = locationObject.getStr("name");

            JSONObject nowObject = resultsObject.getJSONObject("now");
            String weather = nowObject.getStr("text");
            String temperature = nowObject.getStr("temperature");

            String lastUpdate = resultsObject.getStr("last_update");

            System.out.println(name + " " + weather + " " + temperature + " " + lastUpdate);
        }
    }
}
