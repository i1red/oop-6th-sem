package controller.servlet;

import com.google.gson.Gson;
import controller.HttpUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import model.entity.BankAccount;
import model.service.BankAccountService;
import model.service.util.UserClaims;

import java.util.Map;
import java.util.Optional;


@WebServlet("/bank-accounts")
public class BankAccountsServlet extends HttpServlet {
    BankAccountService bankAccountService = new BankAccountService();

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  {
        Optional<UserClaims> claims = HttpUtils.parseAccessTokenClaims(req);
        if (claims.isEmpty()) {
            HttpUtils.writeJsonResponse(
                    resp,
                    HttpUtils.Status.UNAUTHORIZED,
                    Map.of("error", "Incorrect or expired token was provided")
            );
        }
        else {
            BankAccount bankAccount = new Gson().fromJson(req.getReader(), BankAccount.class);
            BankAccount insertedBankAccount = bankAccountService.create(bankAccount.setCustomerId(claims.get().getId()));
            HttpUtils.writeJsonResponse(
                    resp,
                    HttpUtils.Status.OK,
                    insertedBankAccount
            );
        }
    }
}
