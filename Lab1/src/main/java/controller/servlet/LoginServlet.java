package controller.servlet;

import com.google.gson.Gson;
import controller.HttpUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import model.entity.User;
import model.service.UserService;
import model.service.util.TokenUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User gotUser = userService.login(new Gson().fromJson(req.getReader(), User.class));

            HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, TokenUtil.createTokens(gotUser));
        } catch (IllegalArgumentException e) {
            HttpUtil.writeJsonResponseError(resp, HttpUtil.Status.UNAUTHORIZED, e.getMessage());
        }
    }
}
