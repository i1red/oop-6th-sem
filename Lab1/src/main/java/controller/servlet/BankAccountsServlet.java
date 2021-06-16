package controller.servlet;

import com.google.gson.Gson;
import controller.HttpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entity.BankAccount;
import model.service.BankAccountService;
import model.service.util.UserClaims;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


@WebServlet("/bank-accounts")
public class BankAccountsServlet extends HttpServlet {
    private static class UrlParams {
        public static final String USER = "user";
        public static final String ID = "id";
        public static final String BLOCKED = "blocked";
    }

    BankAccountService bankAccountService = new BankAccountService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserClaims claims = HttpUtil.parseAccessTokenClaims(req);
        BankAccount insertedBankAccount = bankAccountService.create(
                new Gson().fromJson(req.getReader(), BankAccount.class).setCustomerId(claims.getId())
        );
        HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, insertedBankAccount);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UserClaims claims = HttpUtil.parseAccessTokenClaims(req);
        if (req.getParameterMap().containsKey(UrlParams.USER)) {
            HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, bankAccountService.listUserBankAccounts(claims.getId()));
        } else if (claims.isAdmin()) {
            HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, bankAccountService.listBankAccounts());
        } else {
            HttpUtil.writeJsonResponseError(resp, HttpUtil.Status.FORBIDDEN, "Only admin users can get all bank accounts");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserClaims claims = HttpUtil.parseAccessTokenClaims(req);
        Optional<Map<String, Object>> parameters = parseDoPutParams(req.getParameterMap());

        if (parameters.isPresent()) {
            int accountId = (int) parameters.get().get(UrlParams.ID);
            boolean blocked = (boolean) parameters.get().get(UrlParams.BLOCKED);

            if (claims.isAdmin()) {
                HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, bankAccountService.bankAccountSetBlocked(accountId, blocked));
            } else if (blocked) {
                try {
                    HttpUtil.writeJsonResponse(resp, HttpUtil.Status.OK, bankAccountService.bankAccountSetBlocked(accountId, true, claims.getId()));
                } catch (IllegalArgumentException e) {
                    HttpUtil.writeJsonResponseError(resp, HttpUtil.Status.CONFLICT, e.getMessage());
                }
            } else {
                HttpUtil.writeJsonResponseError(resp, HttpUtil.Status.FORBIDDEN, "Only admin can unblock bank account");
            }
        } else {
            HttpUtil.writeJsonResponseError(resp, HttpUtil.Status.BAD_REQUEST,
                    String.format("Invalid %s or %s parameters", UrlParams.ID, UrlParams.BLOCKED));
        }
    }

    private Optional<Map<String, Object>> parseDoPutParams(Map<String, String[]> parameters) {
        if (parameters.containsKey(UrlParams.ID) && parameters.containsKey(UrlParams.BLOCKED)) {
            String[] idParams = parameters.get(UrlParams.ID);
            String[] blockedParams = parameters.get(UrlParams.BLOCKED);

            if (idParams.length == 1 && blockedParams.length == 1) {
                try {
                    return Optional.of(Map.of(UrlParams.ID, Integer.parseInt(idParams[0]), UrlParams.BLOCKED, Boolean.parseBoolean(blockedParams[0])));
                } catch (Exception e){
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }
}
