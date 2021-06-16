package controller;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import model.service.util.TokenUtil;
import model.service.util.UserClaims;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtil {
    public static final String AUTH_HEADER = "authorization";
    public static final String AUTH_HEADER_PREFIX = "Bearer ";

    public static class Status {
        public static final int OK = 200;
        public static final int BAD_REQUEST = 400;
        public static final int UNAUTHORIZED = 401;
        public static final int FORBIDDEN = 403;
        public static final int CONFLICT = 409;
    }

    public static void writeJsonResponseError(HttpServletResponse response, int statusCode, String errorMessage) {
        writeJsonResponse(response, statusCode, Map.of("error", errorMessage));
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

    public static Map<String, String> getHeadersMap(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream().collect(Collectors.toMap(name -> name, request::getHeader));
    }

    @SneakyThrows
    public static UserClaims parseAccessTokenClaims(HttpServletRequest request) {
        String token = getHeadersMap(request).get(AUTH_HEADER).substring(AUTH_HEADER_PREFIX.length());
        return TokenUtil.parseAccessToken(token);
    }

    @SneakyThrows
    public static UserClaims parseRefreshTokenClaims(HttpServletRequest request) {
        String token = getHeadersMap(request).get(AUTH_HEADER).substring(AUTH_HEADER_PREFIX.length());
        return TokenUtil.parseRefreshToken(token);
    }

}
