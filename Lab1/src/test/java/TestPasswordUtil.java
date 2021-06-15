import model.service.util.PasswordUtil;
import model.service.util.exception.PasswordTransformError;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPasswordUtil {
    @Test
    public void testPasswordService() throws PasswordTransformError {
        String password = "p4ss_W0rd";
        String encryptedPassword = PasswordUtil.encryptPassword(password);
        String decryptedPassword = PasswordUtil.decryptPassword(encryptedPassword);

        assertNotEquals(password, encryptedPassword);
        assertEquals(password, decryptedPassword);
    }
}
