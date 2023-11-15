package com.specificgroup.blog.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    Long postId;
    Long userId;
    String username;
    String title;
    String text;
    String creationDate;
    String modificationDate;
}