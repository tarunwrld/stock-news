package com.stocknews.stock_news.googlenewsservice;

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
public class GoogleNewsService {
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private final AtomicReference<List<NewsEntity>> indexStockNewsList = new AtomicReference<>(new ArrayList<>());

    private String currentSymbol;

    public void setCurrentSymbol(String symbol) {
        this.currentSymbol = symbol;
    }

    public void getlatest() {
        if (currentSymbol == null || currentSymbol.isEmpty()) {
            throw new IllegalArgumentException("Symbol must be set before updating stock info");
        }
        try {
            String url = "https://www.google.com/finance/quote/" + currentSymbol + ":NSE";
            String response = getStockNews(url);
            Document doc = Jsoup.parse(response);

            Elements elementsClass1 = doc.getElementsByClass("nkXTJ W8knGc");
            Elements elementsClass2 = doc.getElementsByClass("Yfwt5");
            Elements thumbnails = doc.getElementsByClass("Z4idke");

            List<String> stockHeading = elementsClass1.stream()
                    .map(Element::text)
                    .distinct()
                    .collect(Collectors.toList());
            List<String> stockContent = elementsClass2.stream()
                    .map(Element::text)
                    .distinct()
                    .collect(Collectors.toList());

            List<String> imgthumbnail = thumbnails.stream()
                .map(article -> {
                    Element imgTag = article.selectFirst("img.Z4idke");
                    return (imgTag != null) ? imgTag.attr("src") : null;
                })
                .distinct()
                .collect(Collectors.toList());


            System.out.println("Stock Names:");
            stockHeading.forEach(System.out::println);

            System.out.println("Stock Prices:");
            stockContent.forEach(System.out::println);

            List<NewsEntity> stockNewsList = new ArrayList<>();
            for (int i = 0; i < stockHeading.size(); i++) {
                String heading = stockHeading.get(i);
                String content = i < stockContent.size() ? stockContent.get(i) : "Not found";
                String img = i < imgthumbnail.size() ? imgthumbnail.get(i) : "Not found";
                stockNewsList.add(new NewsEntity(heading, content,img));
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
