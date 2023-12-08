package com.specificgroup.notification.util;

import com.specificgroup.notification.dto.MessageType;

public class FilePathUtil {
    public static String getTemplatePath(MessageType type) {
        return switch (type) {
            case REGISTRATION -> "user/registration_template.ftl";
            case PASSWORD_CHANGE -> "user/password_changing_template.ftl";
        };
    }
}
