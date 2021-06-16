package controller.filter;

import controller.HttpUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import model.service.util.TokenUtil;
import model.service.util.UserClaims;
import model.service.util.exception.TokenValidationException;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebFilter(urlPatterns = {"/bank-accounts", "/cards", "/payments"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Map<String, String> headers = HttpUtil.getHeadersMap((HttpServletRequest) request);

        if (!headers.containsKey(HttpUtil.AUTH_HEADER)) {
            HttpUtil.writeJsonResponseError((HttpServletResponse) response, HttpUtil.Status.UNAUTHORIZED,"No authorization info was provided");
        } else if (!validateAuthHeader(headers.get(HttpUtil.AUTH_HEADER))) {
            HttpUtil.writeJsonResponseError((HttpServletResponse) response, HttpUtil.Status.UNAUTHORIZED, "Invalid authorization info was provided");
        }
        else {
            chain.doFilter(request, response);
        }

    }

    private boolean validateAuthHeader(String authHeader) {
        if (authHeader.startsWith(HttpUtil.AUTH_HEADER_PREFIX)) {
            try {
                TokenUtil.parseAccessToken(authHeader.substring(HttpUtil.AUTH_HEADER_PREFIX.length()));
                return true;
            } catch (TokenValidationException e) {
                return false;
            }
        }
        return false;
    }
}
