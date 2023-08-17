package com.flyinterface.controller;

import cn.hutool.json.JSONUtil;
import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ErrorCode;
import com.flyCommon.common.ResultUtils;
import com.flyCommon.exception.BusinessException;
import com.flyinterface.entity.Picture;
import com.flyinterface.entity.Request.RandomPictureRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequestMapping( "/picture" )
@RestController
public class PictureController {

    /**
     * 随机抓取必应上的图片
     *
     * @param randomPicture
     * @return
     */
    @PostMapping( "/bing/randomPicture" )
    public BaseResponse<Picture> randomPicture(@RequestBody RandomPictureRequest randomPicture) {
        if (randomPicture == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String searchText = randomPicture.getSearchText();
        int pageSize;
        Random random = new Random();
        pageSize = random.nextInt(100);
        String url = String.format("https://www.bing.com/images/search?q=%s&first=%s", searchText, pageSize);
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .header("Referer", "https://www.bing.com/")
                    .timeout(1000)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException("输入错误");
        }

        List<Picture> list = new ArrayList<>();
        Elements select = doc.select(".iuscp.isv ");
        for (Element element : select) {
            // 图片地址
            String s = element.select(".iusc").get(0).attr("m");
            Map map = JSONUtil.toBean(s, Map.class);

            String murl = (String) map.get("murl");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            list.add(picture);
        }
//        Random random2 = new Random();
//        int randomIndex = random2.nextInt(count);

        return ResultUtils.success(list.get(0));
    }


}
