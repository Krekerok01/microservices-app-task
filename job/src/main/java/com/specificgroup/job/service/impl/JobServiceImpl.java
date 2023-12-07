package com.specificgroup.job.service.impl;

import com.specificgroup.job.dto.VacancyResponse;
import com.specificgroup.job.service.JobService;
import com.specificgroup.job.util.logger.Logger;
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
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final Logger logger;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VacancyResponse> findVacancies() {
        logger.info("Receiving vacancies...");
        List<VacancyResponse> responseList = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jobsearch4.p.rapidapi.com/api/v2/Jobs/Latest"))
                .header("X-RapidAPI-Key", "282b39271cmsha8d3e3cae2dc2d1p112500jsnd0941072cd85")
                .header("X-RapidAPI-Host", "jobsearch4.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            responseList = processJobSearchServiceResponse(response.body());
        } catch (IOException | InterruptedException e) {
            //replace !!!!!!!
            throw new RuntimeException("Data receiving problems.");
        }
        return responseList;
    }


    private List<VacancyResponse> processJobSearchServiceResponse(String body) {
        JsonObject jsonObject = Json.createReader(new StringReader(body))
            .readObject();

        JsonArray array = jsonObject.getJsonArray("data");
        return convertJSONObjectToVacancyResponseList(array, new ArrayList<VacancyResponse>());
    }

    private List<VacancyResponse> convertJSONObjectToVacancyResponseList(JsonArray array, List<VacancyResponse> responseList) {
        for (JsonValue jsonValue: array){
            JsonObject jsonOb = jsonValue.asJsonObject();
            responseList.add(VacancyResponse.builder()
                    .title(jsonOb.getString("title"))
                    .url(jsonOb.getString("url"))
                    .companyName(jsonOb.getString("company"))
                    .jobSource(jsonOb.getString("jobSource"))
                    .build());
        }
        return responseList;
    }
}
