package poc;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShiroPocTest {

    private Subject subject;

    private void bootstrap(String iniClasspathLocation) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(iniClasspathLocation);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        subject = SecurityUtils.getSubject();
    }

    @AfterEach
    void cleanup() {
        if (subject != null && subject.isAuthenticated()) {
            subject.logout();
        }
    }

    @Test
    void successfulLoginAndAuthorizationChecks() {
        bootstrap("classpath:shiro.ini");

        assertFalse(subject.isAuthenticated());

        UsernamePasswordToken token = new UsernamePasswordToken("alice", "password123");
        subject.login(token);

        assertTrue(subject.isAuthenticated());
        assertTrue(subject.hasRole("admin"));
        assertTrue(subject.isPermitted("document:read"));
        assertTrue(subject.isPermitted("anything:any")); // admin = *

        subject.logout();
        assertFalse(subject.isAuthenticated());
    }

    @Test
    void failedLogin() {
        bootstrap("classpath:shiro.ini");

        UsernamePasswordToken token = new UsernamePasswordToken("bob", "wrong-password");
        assertThrows(AuthenticationException.class, () -> subject.login(token));
        assertFalse(subject.isAuthenticated());
    }

    @Test
    void userRoleHasLimitedPermissions() {
        bootstrap("classpath:shiro.ini");

        subject.login(new UsernamePasswordToken("bob", "secret"));
        assertTrue(subject.isAuthenticated());

        assertTrue(subject.hasRole("user"));
        assertTrue(subject.isPermitted("document:read"));
        assertFalse(subject.isPermitted("document:write"));
    }
}
