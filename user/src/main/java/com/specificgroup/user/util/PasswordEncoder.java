package com.specificgroup.user.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public final class PasswordEncoder {
    public static String encode(String password) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        KeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                65536,
                128
        );
        byte[] hash;
        try {
            hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return new String(hash);
    }
}
