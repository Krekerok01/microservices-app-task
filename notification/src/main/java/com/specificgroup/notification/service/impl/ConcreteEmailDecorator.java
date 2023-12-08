package com.specificgroup.notification.service.impl;

import com.specificgroup.notification.dto.NotifyEvent;
import com.specificgroup.notification.exception.TemplateProcessingException;
import com.specificgroup.notification.service.EmailDecorator;
import com.specificgroup.notification.util.FilePathUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConcreteEmailDecorator implements EmailDecorator {

    private final Configuration configuration;

    @Override
    public String modifyEmailContent(NotifyEvent message) {
        try {
            StringWriter stringWriter = new StringWriter();
            Map<String, Object> model = new HashMap<>();
            model.put("username", message.getUsername());
            configuration.getTemplate(
                            FilePathUtil.getTemplatePath(message.getMessageType())
                    )
                    .process(model, stringWriter);
            return stringWriter.getBuffer().toString();
        } catch (TemplateException | IOException e) {
            throw new TemplateProcessingException(e.getMessage());
        }
    }
}
