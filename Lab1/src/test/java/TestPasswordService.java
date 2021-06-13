import model.service.util.PasswordService;
import model.service.util.exception.PasswordTransformException;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPasswordService {
    @Test
    public void testPasswordService() throws PasswordTransformException {
        String password = "p4ss_W0rd";
        String encryptedPassword = PasswordService.encryptPassword(password);
        String decryptedPassword = PasswordService.decryptPassword(encryptedPassword);

        assertNotEquals(password, encryptedPassword);
        assertEquals(password, decryptedPassword);
    }
}
