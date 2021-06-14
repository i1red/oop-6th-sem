package controller;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import model.service.util.TokenService;
import model.service.util.UserClaims;
import model.service.util.exception.TokenValidationException;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpUtils {
    public static class Status {
        public static final int OK = 200;
        public static final int UNAUTHORIZED = 401;
        public static final int CONFLICT = 409;
    }

    public static void writeJsonResponse(HttpServletResponse response, int statusCode, Object body) {
        writeJsonResponse(response, statusCode, new Gson().toJson(body));
    }

    @SneakyThrows
    public static void writeJsonResponse(HttpServletResponse response, int statusCode, String body) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().print(body);
    }

    private static Map<String, String> getHeadersMap(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream().collect(Collectors.toMap(name -> name, request::getHeader));
    }

    public static Optional<UserClaims> parseAccessTokenClaims(HttpServletRequest request) {
        Optional<String> token = getTokenFromRequest(request);
        try {
            return token.isPresent() ? Optional.of(TokenService.parseAccessToken(token.get())) : Optional.empty();
        } catch (TokenValidationException e) {
            return Optional.empty();
        }
    }

    public static Optional<UserClaims> parseRefreshTokenClaims(HttpServletRequest request) {
        Optional<String> token = getTokenFromRequest(request);
        try {
            return token.isPresent() ? Optional.of(TokenService.parseRefreshToken(token.get())) : Optional.empty();
        } catch (TokenValidationException e) {
            return Optional.empty();
        }
    }

    private static Optional<String> getTokenFromRequest(HttpServletRequest request) {
        Map<String, String> headers = getHeadersMap(request);
        if (!headers.containsKey("authorization")) {
            return Optional.empty();
        }
        String authorization = headers.get("authorization");
        if (!authorization.startsWith("Bearer")) {
            return Optional.empty();
        }
        String token = authorization.substring(7);
        return Optional.of(token);
    }


}
