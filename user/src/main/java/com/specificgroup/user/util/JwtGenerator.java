package com.specificgroup.user.util;

import com.specificgroup.user.model.dto.UserAuthDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtGenerator {

    @Value("${security.jwt.secret}")
    private String secret;

    public String generate(UserAuthDto dto) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", dto.getId());
        extraClaims.put("role", dto.getRole());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setHeader(map)
                .setSubject(dto.getEmail())
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }
}
