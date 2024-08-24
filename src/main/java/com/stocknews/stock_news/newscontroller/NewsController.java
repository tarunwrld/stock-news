package com.stocknews.stock_news.newscontroller;

import com.stocknews.stock_news.googlenewsservice.GoogleNewsService;
import com.stocknews.stock_news.mintnewsentity.MintNews;
import com.stocknews.stock_news.newsentity.NewsEntity;
import com.stocknews.stock_news.newsservice.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private GoogleNewsService googleNewsService;

    @GetMapping("/api/test")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }

    @GetMapping("/get-news")
    public List<MintNews> getAll(){
        newsService.getlatest();
        return newsService.getall();
    }

    @GetMapping("/get-google-news/{symbol}")
    public List<NewsEntity> getNews(@PathVariable String symbol){
        googleNewsService.setCurrentSymbol(symbol);
        googleNewsService.getlatest();
        return googleNewsService.getall();
    }
}
