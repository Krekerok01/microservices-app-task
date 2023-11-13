package com.specificgroup.news.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class NewsResponse {
    String sourceName;
    String title;
    String url;
    String publishedAt;
}