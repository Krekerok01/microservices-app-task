package com.specificgroup.blog.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExceptionResponse {
    String message;
    int statusCode;
    String statusMessage;
}