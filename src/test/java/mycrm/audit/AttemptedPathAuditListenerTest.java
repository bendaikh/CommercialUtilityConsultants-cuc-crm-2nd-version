package mycrm.audit;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

@RunWith(MockitoJUnitRunner.class)
public class AttemptedPathAuditListenerTest {

    @InjectMocks
    private AttemptedPathAuditListener attemptedPathAuditListener;

    @Mock
    private AuthorizedEvent event;

    @Mock
    private Authentication authentication;

    @Test
    public void shouldReturnApplicationEventFailureWithDetailsPresent() {
        Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
        attributes.add(new SecurityConfig("SUPERADMIN"));

        AccessDeniedException accessDeniedException = new AccessDeniedException("Access denied");

        String principalName = "imran.miskeen";
        String ipAddress = "192.168.0.1";

        when(authentication.getName()).thenReturn(principalName);
        when(authentication.getDetails()).thenReturn(ipAddress);

        AuthorizationFailureEvent failureEvent = new AuthorizationFailureEvent(aFilterInvocation(), attributes,
                authentication, accessDeniedException);

        attemptedPathAuditListener.onApplicationEvent(failureEvent);
    }

    @Test
    public void shouldReturnApplicationEventFailureWithDetailsNull() {

        Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
        attributes.add(new SecurityConfig("SUPERADMIN"));

        AccessDeniedException accessDeniedException = new AccessDeniedException("Access denied");

        String principalName = "imran.miskeen";

        when(authentication.getName()).thenReturn(principalName);
        when(authentication.getDetails()).thenReturn(null);

        AuthorizationFailureEvent failureEvent = new AuthorizationFailureEvent(aFilterInvocation(), attributes,
                authentication, accessDeniedException);

        attemptedPathAuditListener.onApplicationEvent(failureEvent);
    }

    private FilterInvocation aFilterInvocation() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        MockHttpServletRequest request = new MockHttpServletRequest();

        return new FilterInvocation(request, response, chain);
    }

    @Test
    public void shouldReturnApplicationEventSuccess() {
        attemptedPathAuditListener.onApplicationEvent(event);
    }

}
