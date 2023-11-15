package com.specificgroup.blog.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import liquibase.pro.packaged.S;

public class JwtUtil {

    public static Long getUserIdFromToken(String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            String jwtWithoutSignature = jwt.substring(0, jwt.lastIndexOf('.') + 1);
            Double userId = Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(jwtWithoutSignature)
                    .getBody()
                    .get("userId", Double.class);
            return userId.longValue();
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Token has expired!");
        }
    }

}
