package com.specificgroup.job.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExceptionResponse {
    String message;
}