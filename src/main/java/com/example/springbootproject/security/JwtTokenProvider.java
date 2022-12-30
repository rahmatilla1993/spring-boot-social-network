package com.example.springbootproject.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.springbootproject.security.SecurityConstants.EXPIRATION_TIME;
//import static com.example.springbootproject.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secretKey}")
    private String SECRET;

    public String createToken(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();
        Date now = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token);
            return true;
        }catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException exception) {

            LOGGER.error(exception.getMessage());
            return false;
        }
    }

    public String getUserNameFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
