package mycrm.audit;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import mycrm.models.User;
import mycrm.models.UserStat;
import mycrm.services.UserService;
import mycrm.services.UserStatsService;

@RunWith(MockitoJUnitRunner.class)
public class LoginAttemptsLoggerTest {

    @InjectMocks
    private LoginAttemptsLogger loginAttemptsLogger;

    @Mock
    private UserService userService;

    @Mock
    private UserStatsService userStatsService;

    @Test
    public final void shouldRecordAuditEventWhenUserLogsIn() {

        
        MockHttpSession session = new MockHttpSession();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(MockHttpServletRequest.DEFAULT_SERVER_ADDR);
        request.setRequestURI("/go/yup");
        request.setSession(session);

        WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetails(request);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("details", webAuthenticationDetails);

        AuditApplicationEvent auditApplicationEvent = new AuditApplicationEvent("imran.miskeen",
                "AUTHENTICATION_SUCCESS", data);
        
        User user = new User();
        user.setId(43l);
        user.setUsername("imran.miskeen");
        
        UserStat userStat = new UserStat();
        userStat.setUser(user);
        userStat.setUsername(user.getUsername());
        userStat.setIpAddress("127.0.0.1");
        userStat.setSessionId(webAuthenticationDetails.getSessionId());

        when(userService.findUserByUsername(any(String.class))).thenReturn(user);

        loginAttemptsLogger.auditEventHappened(auditApplicationEvent);
        verify(userStatsService, times(1)).create(any(UserStat.class));
    }

    @Test
    public final void shouldRecordAuditEventWhenAnonymousUserLogsIn() {
        MockHttpSession session = new MockHttpSession();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(MockHttpServletRequest.DEFAULT_SERVER_ADDR);
        request.setRequestURI("/go/yup");
        request.setSession(session);

        WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetails(request);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("details", webAuthenticationDetails);

        AuditApplicationEvent auditApplicationEvent = new AuditApplicationEvent("anonymousUser",
                "AUTHENTICATION_FAILURE", data);

        loginAttemptsLogger.auditEventHappened(auditApplicationEvent);
        
        verify(userStatsService, times(1)).create(any(UserStat.class));
    }

}
