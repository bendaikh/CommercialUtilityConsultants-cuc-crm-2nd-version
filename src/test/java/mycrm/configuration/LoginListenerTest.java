package mycrm.configuration;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@RunWith(MockitoJUnitRunner.class)
public class LoginListenerTest {

    @InjectMocks
    private LoginListener loginListener;
    
    @Test
    public final void testOnApplicationEvent() {
        AuthenticationSuccessEvent event = mock(AuthenticationSuccessEvent.class);
        
        loginListener.onApplicationEvent(event);
    }

}
