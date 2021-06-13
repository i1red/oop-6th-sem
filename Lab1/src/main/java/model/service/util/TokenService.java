package model.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import model.database.RefreshTokenDBClient;
import model.entity.User;
import model.service.UserService;
import model.service.util.exception.TokenCreationException;
import model.service.util.exception.TokenValidationException;

import java.sql.*;
import java.time.Instant;
import java.util.Date;
import java.util.Map;


public class TokenService {
    private static final String ACCESS_TOKEN_SECRET_KEY = System.getenv("ACCESS_TOKEN_SECRET_KEY");
    private static final String REFRESH_TOKEN_SECRET_KEY = System.getenv("REFRESH_TOKEN_SECRET_KEY");

    private static final int ACCESS_TOKEN_LIFETIME = Integer.parseInt(System.getenv("ACCESS_TOKEN_LIFETIME"));
    private static final int REFRESH_TOKEN_LIFETIME = Integer.parseInt(System.getenv("REFRESH_TOKEN_LIFETIME"));

    private static final String ID = "id";
    private static final String IS_ADMIN = "isAdmin";

    public static String createAccessToken(User user) {
        return Jwts.builder()
                .setClaims(userToClaimsMap(user))
                .setExpiration(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_LIFETIME)))
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET_KEY.getBytes()))
                .compact();
    }

    public static String createRefreshToken(User user) throws TokenCreationException {
        String token = Jwts.builder()
                .setClaims(userToClaimsMap(user))
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

    public static UserClaims parseAccessToken(String token) throws TokenValidationException {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            return claimsToMap(claims.getBody());
        }
        catch (Exception e) {
            throw new TokenValidationException("Invalid token", e);
        }
    }

    public static UserClaims parseRefreshToken(String token) throws TokenValidationException {
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
            return claimsToMap(claims.getBody());
        }
        catch (Exception e) {
            throw new TokenValidationException("Invalid token", e);
        }
    }

    private static Map<String, Object> userToClaimsMap(User user) {
        return Map.of(ID, user.getId().toString(), IS_ADMIN, user.isAdmin());
    }

    private static UserClaims claimsToMap(Claims claims) {
        return new UserClaims().setId(Integer.parseInt((String) claims.get(ID))).setAdmin((boolean)claims.get(IS_ADMIN));
    }
}
