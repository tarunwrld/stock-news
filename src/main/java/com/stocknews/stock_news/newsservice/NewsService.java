package com.stocknews.stock_news.newsservice;

import com.stocknews.stock_news.newsentity.NewsEntity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class NewsService {

    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private final AtomicReference<List<NewsEntity>> indexStockNewsList = new AtomicReference<>(new ArrayList<>());

    public void getlatest() {
        try {
            String url1 = "https://www.livemint.com/market/stock-market-news";
            String response = getStockNews(url1);
            Document doc = Jsoup.parse(response);
            
            Elements headlines = doc.getElementsByClass("headline");
            Elements thumbnails = doc.getElementsByClass("thumbnail");
            
            List<String> stockHeadlines = headlines.stream()
                    .map(Element::text)
                    .distinct()
                    .collect(Collectors.toList());
            
            List<String> stockThumbnails = thumbnails.stream()
                    .map(article -> {
                        Element imgTag = article.selectFirst("img");
                        return (imgTag != null) ? imgTag.attr("src") : null;
                    })
                    .filter(url -> url1 != null && !url.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());
            
            System.out.println("Headlines:");
            stockHeadlines.forEach(System.out::println);

            System.out.println("Thumbnails:");
            stockThumbnails.forEach(System.out::println);
            
            List<NewsEntity> stockNewsList = new ArrayList<>();
            for (int i = 0; i < stockHeadlines.size(); i++) {
                String headline = stockHeadlines.get(i);
                String thumbnail = i < stockThumbnails.size() ? stockThumbnails.get(i) : "Not found";
                stockNewsList.add(new NewsEntity(headline, thumbnail));
            }

            indexStockNewsList.set(stockNewsList);

        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    public List<NewsEntity> getall() {
        return indexStockNewsList.get();
    }

    private String getStockNews(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}
