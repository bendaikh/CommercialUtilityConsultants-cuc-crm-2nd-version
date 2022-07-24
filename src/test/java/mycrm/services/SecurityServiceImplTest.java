package mycrm.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RunWith(MockitoJUnitRunner.class)
public class SecurityServiceImplTest {

    @InjectMocks
    private SecurityServiceImpl securityServiceImpl;

    @Mock
    private AuthenticationManager mockedAuthenticationManager;

    @Mock
    private UserDetailsService mockedUserDetailsService;

    @Test
    public void findLoggedInUsername() {
        
        UserDetails expectedPrincipal = new User("user", "password", new ArrayList<GrantedAuthority>());        
        
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);        
        when(authentication.getDetails()).thenReturn(expectedPrincipal);
        when(authentication.getPrincipal()).thenReturn(expectedPrincipal);

        SecurityContextHolder.setContext(securityContext);

        String foundUsername = securityServiceImpl.findLoggedInUsername();

        assertEquals("user", foundUsername);
    }
    
    @Test
    public void findNullUsernameLoggedInUsername() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String foundUsername = securityServiceImpl.findLoggedInUsername();

        assertNull(foundUsername);
    }
    
    @Test
    public void shouldAutoLoginWhenAuthenticated() {        
        UserDetails expectedPrincipal = new User("imran.miskeen", "xxx", new ArrayList<GrantedAuthority>());  
        
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                expectedPrincipal, "xxx", expectedPrincipal.getAuthorities());
        
        when(mockedUserDetailsService.loadUserByUsername(any(String.class))).thenReturn(expectedPrincipal);
        
        securityServiceImpl.autologin("imran.miskeen", "xxx");
        
        verify(mockedAuthenticationManager, times(1)).authenticate(usernamePasswordAuthenticationToken);
    }
    
    @Test
    public void shouldNotAutoLoginWhenNotAuthenticated() {         
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, "");
        
        when(mockedUserDetailsService.loadUserByUsername("imran.miskeen")).thenReturn(null);
        
        securityServiceImpl.autologin("imran.miskeen", "xxx");
        
        verify(mockedAuthenticationManager, times(0)).authenticate(usernamePasswordAuthenticationToken);
    }

}
