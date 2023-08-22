package com.flyinterface.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.flyCommon.common.BaseResponse;

import com.flyCommon.common.ErrorCode;
import com.flyCommon.common.ResultUtils;
import com.flyCommon.exception.BusinessException;
import com.flyinterface.entity.WangYiYunMusic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping( "/music" )
@RestController
public class MusicController {

    /**
     * 网易云热歌榜随机
     * @return
     */
    @GetMapping( "/wangyiyun/rege" )
    public BaseResponse<WangYiYunMusic> wangyiyunRandomMusic() {
        String url = "https://api.uomg.com/api/rand.music?format=json";
        String jsonStr = HttpUtil.get(url);
        if (jsonStr == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject data = jsonObject.getJSONObject("data");
        String name = data.getStr("name");
        String url1 = data.getStr("url");
        String picurl = data.getStr("picurl");
        WangYiYunMusic yiYunMusic = new WangYiYunMusic();
        yiYunMusic.setName(name);
        yiYunMusic.setUrl(url1);
        yiYunMusic.setPictureUrl(picurl);

        return ResultUtils.success(yiYunMusic);
    }
}
