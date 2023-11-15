package com.specificgroup.news.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExceptionResponse {
    String message;
    int statusCode;
    String statusMessage;
}