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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User insertedUser = userService.register(new Gson().fromJson(req.getReader(), User.class));

            HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, TokenUtil.createTokens(insertedUser));
        } catch (IllegalArgumentException e) {
            HttpUtil.writeJsonResponseError(resp, HttpUtil.Status.CONFLICT, e.getMessage());
        }
    }
}
