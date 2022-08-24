package mycrm.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LogManager.getLogger();

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendSimpleMessage(String[] to, String subject, String text) {
        try {
            String[] sendTo = to;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(sendTo);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("Commercial Utility Consultants <no-reply@commercialutilityconsultants.co.uk>");

            javaMailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendMimeMessage(String[] to, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            String[] sendTo = to;
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("Commercial Utility Consultants <no-reply@commercialutilityconsultants.co.uk>");
            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setText(text, true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
