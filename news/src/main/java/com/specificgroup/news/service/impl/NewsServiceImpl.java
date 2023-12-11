package com.specificgroup.news.service.impl;

import com.specificgroup.news.dto.NewsResponse;
import com.specificgroup.news.exception.ServiceUnavailableException;
import com.specificgroup.news.service.NewsService;
import com.specificgroup.news.util.logger.Logger;
import lombok.RequiredArgsConstructor;
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

import static com.specificgroup.news.util.Constants.Message.DATA_RECEIVING_EXCEPTION;
import static com.specificgroup.news.util.Constants.UrlPath.RAPID_API_URL;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final Logger logger;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NewsResponse> findCurrentNews() {
        logger.info("Receiving news...");
        try {

            HttpRequest request = buildGetNewsRequest();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400 && response.statusCode() <= 599) throw new IOException();
            return processNewsApiServiceResponse(response.body());
        } catch (IOException | InterruptedException e) {
            logger.error(DATA_RECEIVING_EXCEPTION);
            throw new ServiceUnavailableException(DATA_RECEIVING_EXCEPTION);
        }
    }

    private static HttpRequest buildGetNewsRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(RAPID_API_URL))
                .header("X-RapidAPI-Key", "282b39271cmsha8d3e3cae2dc2d1p112500jsnd0941072cd85")
                .header("X-RapidAPI-Host", "newsi-api.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    private List<NewsResponse> processNewsApiServiceResponse(String body) {
        JsonArray array = Json.createReader(new StringReader(body))
                .readArray();

        return convertJSONObjectToNewsResponseList(array, new ArrayList<NewsResponse>());
    }

    private List<NewsResponse> convertJSONObjectToNewsResponseList(JsonArray array, List<NewsResponse> responseList) {
        for (JsonValue jsonValue: array){
            JsonObject jsonOb = jsonValue.asJsonObject();
            responseList.add(NewsResponse.builder()
                    .sourceName(jsonOb.getString("sourceName"))
                    .title(jsonOb.getString("title"))
                    .url(jsonOb.getString("link"))
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