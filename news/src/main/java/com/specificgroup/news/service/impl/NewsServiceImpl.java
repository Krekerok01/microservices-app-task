package com.specificgroup.news.service.impl;

import com.specificgroup.news.dto.NewsResponse;
import com.specificgroup.news.exception.ReceiveDataException;
import com.specificgroup.news.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NewsServiceImpl implements NewsService {

    @Override
    public List<NewsResponse> findCurrentNews() {
        log.info("Receiving news...");
        List<NewsResponse> responseList = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=b2538538ca3c4f9d9acdcf82b85950b8"))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            responseList = processNewsapiServiceResponse(response.body());
        } catch (IOException | InterruptedException e) {
            throw new ReceiveDataException("Data receiving problems.");
        }
        return responseList;
    }

    private List<NewsResponse> processNewsapiServiceResponse(String body) {
        JsonObject jsonObject = Json.createReader(new StringReader(body))
                .readObject();

        JsonArray array = jsonObject.getJsonArray("articles");
        return convertJSONObjectToNewsResponseList(array, new ArrayList<NewsResponse>());
    }

    private List<NewsResponse> convertJSONObjectToNewsResponseList(JsonArray array, List<NewsResponse> responseList) {
        for (JsonValue jsonValue: array){
            JsonObject jsonOb = jsonValue.asJsonObject();
            responseList.add(NewsResponse.builder()
                    .sourceName(jsonOb.getJsonObject("source").getString("name"))
                    .title(jsonOb.getString("title"))
                    .url(jsonOb.getString("url"))
                    .publishedAt(formatDate(jsonOb.getString("publishedAt")))
                    .build());
        }
        return responseList;
    }

    private String formatDate(String publishedAt) {
        Instant instant = Instant.parse(publishedAt);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Minsk"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        return  localDateTime.format(formatter);
    }
}