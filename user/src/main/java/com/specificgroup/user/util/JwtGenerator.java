package com.specificgroup.user.util;

import com.specificgroup.user.model.dto.UserAuthDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class JwtGenerator {
    public static String generate(UserAuthDto dto) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("MTA1NDQ2NDYyMjkxODQ3NjI0NjM4NjUxNTYxZGZnMTU2MTQ4ZGY5NDE4MTk0OTg=");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        return Jwts.builder()
                .setHeader(map)
                .claim("userId", dto.getId())
                .claim("role", dto.getRole())
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }
}
