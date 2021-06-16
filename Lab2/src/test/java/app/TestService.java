package app;

import app.model.entity.BankAccount;
import app.model.repository.BankAccountRepository;
import app.model.service.BankAccountService;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class TestBankAccountService {
    @After
    @Before
    void cleanDataBase() {

    }

    @Test
    void testSetBlocked() {
        BankAccount bankAccount = new BankAccount().setId(1).s
    }



}
