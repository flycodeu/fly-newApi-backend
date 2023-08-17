package com.flyinterface.controller;

import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ResultUtils;
import com.flyinterface.entity.DouBanFamousBook;
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
@RequestMapping( "/book" )
public class BookController {

    @GetMapping( "/douban" )
    public BaseResponse<List<DouBanFamousBook>> famousDouBanBook() {
        String url = "https://book.douban.com/";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(document);
        List<DouBanFamousBook> list = new ArrayList<>();
        Elements elements = document.select("ul.list-col2 li");
        for (Element element : elements) {
            String title = element.select("h4.title a").text();
            String coverUrl = element.select("div.cover img").attr("src");
            String rate = element.select("p.entry-star-small span.average-rating").text();
            String author = element.select("p.author").text();


            DouBanFamousBook douBanFamousBook = new DouBanFamousBook();
            douBanFamousBook.setTitle(title);
            douBanFamousBook.setCoverUrl(coverUrl);
            douBanFamousBook.setRate(rate);
            douBanFamousBook.setAuthor(author);
            list.add(douBanFamousBook);

//            System.out.println("书名：" + title);
//            System.out.println("封面：" + coverUrl);
//            System.out.println("评分：" + rate);
//            System.out.println("作者：" + author);
//            System.out.println("------------------------");
        }

        return ResultUtils.success(list);
    }
}
