package controller.servlet;

import com.google.gson.Gson;
import controller.HttpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entity.Card;
import model.service.CardService;
import model.service.util.UserClaims;

import java.io.IOException;

@WebServlet("/cards")
public class CardsServlet extends HttpServlet {
    CardService cardService = new CardService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserClaims claims = HttpUtil.parseAccessTokenClaims(req);

        try {
            HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, cardService.createCard(new Gson().fromJson(req.getReader(), Card.class), claims.getId()));
        } catch (IllegalArgumentException e) {
            HttpUtil.writeJsonResponseError(resp, HttpUtil.Status.CONFLICT, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserClaims claims = HttpUtil.parseAccessTokenClaims(req);

        if (req.getParameterMap().containsKey("user")) {
            HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, cardService.listUserCards(claims.getId()));
        } else {
            HttpUtil.writeJsonResponseError(resp, HttpUtil.Status.FORBIDDEN, "Cannot list all cards");
        }
    }
}
