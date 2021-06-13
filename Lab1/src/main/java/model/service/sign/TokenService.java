package model.service.sign;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import model.database.RefreshTokenDBClient;
import model.entity.User;

import java.sql.*;
import java.time.Instant;
import java.util.Date;


public class TokenService {
    private static final String ACCESS_TOKEN_SECRET_KEY = System.getenv("ACCESS_TOKEN_SECRET_KEY");
    private static final String REFRESH_TOKEN_SECRET_KEY = System.getenv("REFRESH_TOKEN_SECRET_KEY");

    private static final long ACCESS_TOKEN_LIFETIME = Integer.parseInt(System.getenv("ACCESS_TOKEN_LIFETIME"));
    private static final long REFRESH_TOKEN_LIFETIME = Integer.parseInt(System.getenv("REFRESH_TOKEN_LIFETIME"));


    public static String createAccessToken(User user) {
        return Jwts.builder()
                .setId(user.getId().toString())
                .setExpiration(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_LIFETIME)))
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET_KEY.getBytes()))
                .compact();
    }

    public static String createRefreshToken(User user) throws TokenCreationException {
        String token = Jwts.builder()
                .setId(user.getId().toString())
                .setExpiration(Date.from(Instant.now().plusSeconds(REFRESH_TOKEN_LIFETIME)))
                .signWith(Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET_KEY.getBytes()))
                .compact();

        try {
            RefreshTokenDBClient.insert(token);
            return token;
        } catch (SQLException e) {
            throw new TokenCreationException("Failed to save token", e);
        }
    }

    public static Claims parseAccessToken(String token) throws TokenValidationException{
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            return claims.getBody();
        }
        catch (Exception e) {
            throw new TokenValidationException("Invalid token", e);
        }
    }

    public static Claims parseRefreshToken(String token) throws TokenValidationException {
        try {
            if (RefreshTokenDBClient.contains(token)) {
                throw new TokenValidationException("Token is not in database");
            }
        } catch (SQLException e) {
            throw new TokenValidationException("Failed to validate token", e);
        }


        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            RefreshTokenDBClient.delete(token);
            return claims.getBody();
        }
        catch (Exception e) {
            throw new TokenValidationException("Invalid token", e);
        }
    }

}
