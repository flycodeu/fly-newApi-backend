package com.flyinterface.controller;

import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ErrorCode;
import com.flyCommon.common.ResultUtils;
import com.flyCommon.exception.BusinessException;
import com.flyinterface.entity.DouBanFamousFilm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( "/film" )
public class FilmController {

    /**
     * 获取豆瓣电影的爬虫
     *
     * @return
     */
    @GetMapping( "/douban" )
    public BaseResponse<List<DouBanFamousFilm>> getFileByDouBan() {
        String url = "https://movie.douban.com/";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<DouBanFamousFilm> list = new ArrayList<>();
        Elements elements = document.select("li.ui-slide-item");
        for (Element element : elements) {

            // 标题
            String title = element.attr("data-title");
            if (title.isEmpty()) {
                continue; // Skip this iteration if the title is empty
            }
            // 发行时间
            String release = element.attr("data-release");
            // 评分
            String rate = element.attr("data-rate");
            // 主演
            String star = element.attr("data-star");
            // 地址
            String trailer = element.attr("data-trailer");
            // 卖票地址
            String ticket = element.attr("data-ticket");
            // 时长
            String duration = element.attr("data-duration");
            // 发行区域
            String region = element.attr("data-region");
            // 导演
            String director = element.attr("data-director");
            // 演员
            String actors = element.attr("data-actors");

            DouBanFamousFilm douBanFamousFilm = new DouBanFamousFilm();
            douBanFamousFilm.setTitle(title);
            douBanFamousFilm.setRelease(release);
            douBanFamousFilm.setRate(rate);
            douBanFamousFilm.setTrailer(trailer);
            douBanFamousFilm.setDuration(duration);
            douBanFamousFilm.setRegion(region);
            douBanFamousFilm.setActors(actors);

            list.add(douBanFamousFilm);
//            System.out.println("电影名字：" + title);
//            System.out.println("发行时间" + release);
//            System.out.println("评分" + rate);
//            System.out.println("地址" + trailer);
//            System.out.println("时长" + duration);
//            System.out.println("发行地区" + region);
//            System.out.println("主演" + actors);
        }

        return ResultUtils.success(list);
    }
}
