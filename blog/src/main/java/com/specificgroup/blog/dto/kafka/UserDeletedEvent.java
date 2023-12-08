package com.specificgroup.blog.dto.kafka;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeletedEvent {
    private Long userId;
}
