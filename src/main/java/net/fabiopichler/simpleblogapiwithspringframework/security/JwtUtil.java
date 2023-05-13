/*-------------------------------------------------------------------------------

Copyright (c) 2023 Fábio Pichler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

-------------------------------------------------------------------------------*/

package net.fabiopichler.simpleblogapiwithspringframework.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long expiration;

    public JwtUtil(
            @Value("${security.jwt.secret-key}") String secretKey,
            @Value("${security.jwt.expiration}") Long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(/*Decoders.BASE64.decode(secretKey)*/secretKey.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    public String generateToken(long userId) {
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(expiration);
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();

        return Jwts
                .builder()
                .setSubject(String.format("%d", userId))
                .setExpiration(Date.from(instant))
                .signWith(secretKey, SignatureAlgorithm.HS256) // HS512
                .compact();
    }

    public long getUserId(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public long getUserIdExpired(String token) {
        try {
            return getUserId(token);

        } catch (ExpiredJwtException e) {
            return Long.parseLong(e.getClaims().getSubject());
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer "))
            throw new RuntimeException();

        final String token = authorization.substring(7);

        if (token.isEmpty())
            throw new RuntimeException();

        return token;
    }
}
