package com.specificgroup.subscription.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExceptionResponse {
    String message;
    int statusCode;
    String statusMessage;
}