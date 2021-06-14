package controller.servlet;

import com.google.gson.Gson;
import controller.HttpUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import model.entity.User;
import model.service.UserService;
import model.service.util.TokenService;

import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User user = new Gson().fromJson(req.getReader(), User.class);

        try {
            User gotUser = userService.login(user);

            String accessToken = TokenService.createAccessToken(gotUser);
            String refreshToken = TokenService.createRefreshToken(gotUser);

            HttpUtils.writeJsonResponse(
                    resp,
                    HttpUtils.Status.OK,
                    Map.of("accessToken", accessToken,"refreshToken", refreshToken)
            );
        } catch (IllegalArgumentException e) {
            HttpUtils.writeJsonResponse(
                    resp,
                    HttpUtils.Status.UNAUTHORIZED,
                    Map.of("error", e.getMessage())
            );
        }


    }
}
