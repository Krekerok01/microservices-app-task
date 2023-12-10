package com.specificgroup.job.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VacancyResponse {
    String title;
    String url;
    String companyName;
    String jobSource;
}