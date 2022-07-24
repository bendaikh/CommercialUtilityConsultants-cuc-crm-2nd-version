package mycrm.mail;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceImplTest {
    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender mockJavaMailSender;

    @Test
    public void shouldSendEmail() {
        //emailService.sendSimpleMessage("j@jcom", "Hi", "Hello");
        //verify(mockJavaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
