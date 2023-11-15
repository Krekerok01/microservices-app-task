package com.specificgroup.blog.dto.kafka;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserServiceMessage {
    private Long userId;
}
