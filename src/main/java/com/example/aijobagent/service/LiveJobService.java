package com.example.aijobagent.service;

import com.example.aijobagent.dto.LiveJobResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class LiveJobService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String appId;
    private final String appKey;
    private final String country;

    public LiveJobService(RestClient.Builder restClientBuilder,
                          ObjectMapper objectMapper,
                          @Value("${app.adzuna.app-id:}") String appId,
                          @Value("${app.adzuna.app-key:}") String appKey,
                          @Value("${app.adzuna.country:in}") String country) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.appId = appId;
        this.appKey = appKey;
        this.country = country;
    }

    public List<LiveJobResponse> search(String query, String location, int page, int size) {
        if (appId.isBlank() || appKey.isBlank()) {
            throw new IllegalStateException("Live jobs are not configured. Add ADZUNA_APP_ID and ADZUNA_APP_KEY.");
        }

        String uri = UriComponentsBuilder
                .fromUriString("https://api.adzuna.com/v1/api/jobs/{country}/search/{page}")
                .queryParam("app_id", appId)
                .queryParam("app_key", appKey)
                .queryParam("results_per_page", Math.min(size, 50))
                .queryParamIfPresent("what", blankToEmpty(query))
                .queryParamIfPresent("where", blankToEmpty(location))
                .queryParam("content-type", "application/json")
                .buildAndExpand(country, page + 1)
                .toUriString();

        String body = restClient.get().uri(uri).retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new IllegalStateException("Live jobs provider returned HTTP " + response.getStatusCode().value());
                })
                .body(String.class);

        try {
            JsonNode results = objectMapper.readTree(body).path("results");
            List<LiveJobResponse> jobs = new ArrayList<>();
            results.forEach(job -> jobs.add(new LiveJobResponse(
                    job.path("id").asText(),
                    text(job, "title", "Untitled role"),
                    job.path("company").path("display_name").asText("Unknown company"),
                    job.path("location").path("display_name").asText("Location not specified"),
                    text(job, "description", "No description provided."),
                    text(job, "redirect_url", ""),
                    salary(job),
                    text(job, "contract_type", "Job")
            )));
            return jobs;
        } catch (Exception exception) {
            throw new IllegalStateException("Could not read live jobs response.", exception);
        }
    }

    private java.util.Optional<String> blankToEmpty(String value) {
        return value == null || value.isBlank() ? java.util.Optional.empty() : java.util.Optional.of(value.trim());
    }

    private String text(JsonNode node, String field, String fallback) {
        String value = node.path(field).asText("").trim();
        return value.isBlank() ? fallback : value;
    }

    private String salary(JsonNode job) {
        double min = job.path("salary_min").asDouble(0);
        double max = job.path("salary_max").asDouble(0);
        if (min == 0 && max == 0) return "Salary not disclosed";
        if (max == 0) return String.valueOf(Math.round(min));
        return Math.round(min) + " - " + Math.round(max);
    }
}
