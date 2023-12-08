package com.specificgroup.user.model.dto.kafka;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDeletedEvent {
    private Long userId;
}