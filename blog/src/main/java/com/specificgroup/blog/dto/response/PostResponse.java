package com.specificgroup.blog.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PostResponse {
    Long postId;
    Long userId;
    String title;
    String text;
    String creationDate;
    String modificationDate;
}