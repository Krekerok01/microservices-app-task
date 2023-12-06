package com.specificgroup.notification.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.specificgroup.notification.dto.message.MessageContent;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MessageContent.class, name = "messageContent")
})
public interface Content {
    String getHeader();
    String getText();
}
