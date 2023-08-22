package com.flyinterface.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ResultUtils;
import com.flyinterface.entity.KuaiShouReSou;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping( "/resou" )
@RestController
public class ReSouController {

    /**
     * 快手热搜
     *
     * @return
     */
    @GetMapping( "/kuaishou" )
    public BaseResponse<List<KuaiShouReSou>> getKuaiShouReSou() {
        String url = "https://api.gumengya.com/Api/KuaiShouHot";
        String jsonStr = HttpUtil.get(url);
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray data = jsonObject.getJSONArray("data");
        List<KuaiShouReSou> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject entries = data.getJSONObject(i);
            String title = entries.getStr("title");
            String hot = entries.getStr("hot");
            String url1 = entries.getStr("url");
            String updatetime = entries.getStr("updatetime");

            KuaiShouReSou kuaiShouReSou = new KuaiShouReSou();
            kuaiShouReSou.setTitle(title);
            kuaiShouReSou.setHot(hot);
            kuaiShouReSou.setUrl(url1);
            kuaiShouReSou.setUpdateTime(updatetime);

            list.add(kuaiShouReSou);
        }

        return ResultUtils.success(list);
    }
}
