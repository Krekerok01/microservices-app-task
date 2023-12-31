package com.specificgroup.user.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Util class for password encoding
 */
public final class PasswordEncoder {
    public static String encode(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
