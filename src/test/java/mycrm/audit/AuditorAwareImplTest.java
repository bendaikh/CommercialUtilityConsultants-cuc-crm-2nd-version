package mycrm.audit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import mycrm.models.LoggedInUser;
import mycrm.models.User;
import mycrm.services.UserService;

@RunWith(MockitoJUnitRunner.class)
public class AuditorAwareImplTest {

    @InjectMocks
    private AuditorAwareImpl auditorAwareImpl;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private LoggedInUser principal;

    @Test
    public void shouldReturnCurrentAuditor() {

        Long id = Long.valueOf(6);

        User loggedInUser = new User();
        loggedInUser.setId(id);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(principal);

        when(principal.getId()).thenReturn(id);

        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(any(Long.class))).thenReturn(loggedInUser);

        User currentAuditor = auditorAwareImpl.getCurrentAuditor();
        assertEquals(loggedInUser, currentAuditor);
        assertEquals(loggedInUser.getId(), currentAuditor.getId());
    }

}
