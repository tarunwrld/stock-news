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
            String url = "https://www.google.com/finance/quote/" + currentSymbol + ":NSE"; // Verify this URL
            String response = getStockNews(url);
            Document doc = Jsoup.parse(response);

            // Select multiple elements for each class
            Elements elementsClass1 = doc.getElementsByClass("nkXTJ W8knGc"); // Adjust this class as needed
            Elements elementsClass2 = doc.getElementsByClass("Yfwt5"); // Adjust this class as needed

            // Process the elements and collect their text
            List<String> stockHeading = elementsClass1.stream()
                    .map(Element::text)
                    .distinct()
                    .collect(Collectors.toList());
            List<String> stockContent = elementsClass2.stream()
                    .map(Element::text)
                    .distinct()
                    .collect(Collectors.toList());

            // For demonstration purposes, print all collected values
            System.out.println("Stock Names:");
            stockHeading.forEach(System.out::println);

            System.out.println("Stock Prices:");
            stockContent.forEach(System.out::println);


            // Create a list of StockInfo objects
            List<NewsEntity> stockNewsList = new ArrayList<>();
            for (int i = 0; i < stockHeading.size(); i++) {
                String heading = stockHeading.get(i);
                String content = i < stockContent.size() ? stockContent.get(i) : "Not found";
                stockNewsList.add(new NewsEntity(heading, content));
            }

            indexStockNewsList.set(stockNewsList);

        } catch (IOException e) {
            // Log error here using a logging framework like SLF4J or Log4j
            e.printStackTrace(); // Optionally keep this for debugging; replace with a logger in production
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
