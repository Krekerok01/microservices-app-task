package com.specificgroup.job.service.impl;

import com.specificgroup.job.dto.VacancyResponse;
import com.specificgroup.job.exception.DataProcessingException;
import com.specificgroup.job.exception.ServiceUnavailableException;
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

import static com.specificgroup.job.util.Constants.Message.*;
import static com.specificgroup.job.util.Constants.UrlPath.RAPID_API_URL;

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
        try {
            HttpRequest request = buildGetVacanciesRequest();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            checkResponseCode(response);
            return processJobSearchServiceResponse(response.body());
        } catch (IOException | InterruptedException e) {
            logger.error(DATA_PROCESSING_EXCEPTION);
            throw new DataProcessingException(DATA_PROCESSING_EXCEPTION);
        }
    }

    private static HttpRequest buildGetVacanciesRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(RAPID_API_URL))
                .header("X-RapidAPI-Key", "282b39271cmsha8d3e3cae2dc2d1p112500jsnd0941072cd85")
                .header("X-RapidAPI-Host", "jobsearch4.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    private void checkResponseCode(HttpResponse<String> response) {
        int responseStatusCode = response.statusCode();

        if (responseStatusCode >= 400 && responseStatusCode <= 599) {
            logger.error(SERVICE_UNAVAILABLE_EXCEPTION);
            throw new ServiceUnavailableException(SERVICE_UNAVAILABLE_EXCEPTION);
        }
    }

    private List<VacancyResponse> processJobSearchServiceResponse(String body) {
        JsonObject jsonObject = Json.createReader(new StringReader(body))
            .readObject();

        JsonArray array = jsonObject.getJsonArray("data");

        if (array.isEmpty()){
            logger.error(SERVICE_UNAVAILABLE_EXCEPTION + " Empty response body.");
            throw new ServiceUnavailableException(SERVICE_UNAVAILABLE_EXCEPTION);
        }

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