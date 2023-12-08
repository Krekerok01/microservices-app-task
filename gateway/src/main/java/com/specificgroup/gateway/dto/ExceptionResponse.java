package com.specificgroup.gateway.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExceptionResponse {
    String message;
}