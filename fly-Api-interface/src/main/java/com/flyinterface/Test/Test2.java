package com.flyinterface.Test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ResultUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Test2 {
    public static void main(String[] args) {
        String searchText = "西瓜";
        int pageSize = 10;
        Random random1 = new Random();
        pageSize = random1.nextInt(10);
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
        int count = 0;
        List<String> list = new ArrayList<>();
        Elements select = doc.select(".iuscp.isv ");
        for (Element element : select) {
            // 图片地址
            String s = element.select(".iusc").get(0).attr("m");
            Map map = JSONUtil.toBean(s, Map.class);
            String murl = (String) map.get("murl");

            String title = element.select(".inflnk").get(0).attr("aria-label");

            list.add(murl + title);
            count++;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(count);
        System.out.println(list.get(randomIndex));

    }
}
