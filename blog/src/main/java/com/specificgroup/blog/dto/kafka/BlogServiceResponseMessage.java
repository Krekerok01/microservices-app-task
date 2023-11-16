package com.specificgroup.blog.dto.kafka;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BlogServiceResponseMessage {
    Long deletedUserId;
    String message;
}
