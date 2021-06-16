package controller.servlet;

import com.google.gson.Gson;
import controller.HttpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entity.Payment;
import model.service.PaymentService;
import model.service.util.UserClaims;

import java.io.IOException;

@WebServlet("/payments")
public class PaymentsServlet extends HttpServlet {
    PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserClaims claims = HttpUtil.parseAccessTokenClaims(req);

        try {
            HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, paymentService.createPayment(new Gson().fromJson(req.getReader(), Payment.class), claims.getId()));
        } catch (IllegalArgumentException e) {
            HttpUtil.writeJsonResponseError(resp, HttpUtil.Status.CONFLICT, e.getMessage());
        }
    }
}
