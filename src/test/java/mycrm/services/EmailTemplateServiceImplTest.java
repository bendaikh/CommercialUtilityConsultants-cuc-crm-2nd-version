package mycrm.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import mycrm.mail.EmailService;
import mycrm.models.Customer;
import mycrm.models.CustomerChildNote;
import mycrm.models.CustomerNote;
import mycrm.models.DiallerAPIResponse;
import mycrm.models.EmailSetting;
import mycrm.models.User;
import mycrm.upload.DiallerDatasetType;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FreeMarkerTemplateUtils.class)
public class EmailTemplateServiceImplTest {
    public static final String EMAIL = "d@gmail.com";

    @InjectMocks
    private EmailTemplateServiceImpl emailTemplateService;

    @Mock
    private EmailService mockEmailService;

    @Mock
    private Configuration mockConfiguration;

    @Mock
    private EmailSettingService mockEmailSettingService;


    @Test
    public void shouldNotSendNotificationWhenNoTaggedUserEmail() throws Exception {
        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(1L);
        customerNote.setTaggedUser(aTaggedUser());

        emailTemplateService.sendTaggedCustomerNoteNotification(customerNote);

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldNotSendNotificationWhenNoCreatedByUserEmail() throws Exception {
        User taggedUser = aTaggedUser();
        taggedUser.setEmail(EMAIL);

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(1L);
        customerNote.setTaggedUser(taggedUser);
        customerNote.setCreatedBy(aCreatedUser());

        emailTemplateService.sendTaggedCustomerNoteNotification(customerNote);

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldNotSendNotificationWhenSenderAndReceiverAreTheSame() throws Exception {
        User taggedUser = aTaggedUser();
        taggedUser.setEmail(EMAIL);

        User createdUser = aCreatedUser();
        createdUser.setEmail(EMAIL);

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(1L);
        customerNote.setTaggedUser(taggedUser);
        customerNote.setCreatedBy(createdUser);

        emailTemplateService.sendTaggedCustomerNoteNotification(customerNote);

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldSendNotification() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        User taggedUser = aTaggedUser();
        taggedUser.setEmail(EMAIL);

        User createdUser = aCreatedUser();
        createdUser.setEmail("xxx@gmail.com");

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(1L);
        customerNote.setTaggedUser(taggedUser);
        customerNote.setCreatedBy(createdUser);
        customerNote.setCustomer(aCustomer());

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        emailTemplateService.sendTaggedCustomerNoteNotification(customerNote);

        verify(mockEmailService, times(1))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldNotSendChildNotificationWhenNoTaggedUserEmail() throws Exception {
        CustomerChildNote customerChildNote = new CustomerChildNote();
        customerChildNote.setId(2L);
        customerChildNote.setReplyTaggedUser(aTaggedUser());

        emailTemplateService.sendTaggedCustomerChildNoteNotification(customerChildNote);

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldNotSendChildNotificationWhenNoCreatedByUserEmail() throws Exception {
        User taggedUser = aTaggedUser();
        taggedUser.setEmail(EMAIL);

        CustomerChildNote customerChildNote = new CustomerChildNote();
        customerChildNote.setId(2L);
        customerChildNote.setReplyTaggedUser(taggedUser);
        customerChildNote.setCreatedBy(aCreatedUser());

        emailTemplateService.sendTaggedCustomerChildNoteNotification(customerChildNote);

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldNotSendChildNotificationWhenSenderAndReceiverAreTheSame() throws Exception {
        User taggedUser = aTaggedUser();
        taggedUser.setEmail(EMAIL);

        User createdUser = aCreatedUser();
        createdUser.setEmail(EMAIL);

        CustomerChildNote customerChildNote = new CustomerChildNote();
        customerChildNote.setId(2L);
        customerChildNote.setReplyTaggedUser(taggedUser);
        customerChildNote.setCreatedBy(createdUser);

        emailTemplateService.sendTaggedCustomerChildNoteNotification(customerChildNote);

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldSendChildNotification() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        User taggedUser = aTaggedUser();
        taggedUser.setEmail(EMAIL);

        User createdUser = aCreatedUser();
        createdUser.setEmail("xxx@gmail.com");

        CustomerNote customerNote = new CustomerNote();
        customerNote.setId(1L);
        customerNote.setTaggedUser(taggedUser);
        customerNote.setCreatedBy(createdUser);
        customerNote.setCustomer(aCustomer());

        CustomerChildNote customerChildNote = new CustomerChildNote();
        customerChildNote.setId(2L);
        customerChildNote.setReplyTaggedUser(taggedUser);
        customerChildNote.setCreatedBy(createdUser);
        customerChildNote.setCustomer(aCustomer());
        customerChildNote.setCustomerNote(customerNote);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        emailTemplateService.sendTaggedCustomerChildNoteNotification(customerChildNote);

        verify(mockEmailService, times(1))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldSendFailedDiallerEmailNotification() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        emailTemplateService.sendDiallerFailedEmailAlert(new DiallerAPIResponse(), DiallerDatasetType.CREATE);

        verify(mockEmailService, times(1))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldSendDiallerUploadNotNeeded() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        emailTemplateService.sendDiallerUploadNotNeeded("Not needed");

        verify(mockEmailService, times(1))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Test
    public void shouldSendDiallerSuccessEmailAlert() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        emailTemplateService.sendDiallerSuccessEmailAlert(new DiallerAPIResponse(), DiallerDatasetType.CREATE);

        verify(mockEmailService, times(1))
                .sendMimeMessage(anyObject(), anyString(), anyString());
    }

    @Ignore
    @Test
    public void shouldSendSimpleSoldEmail() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        EmailSetting emailSetting = simpleEmailSetting();
        when(mockEmailSettingService.getEmailSetting(anyString())).thenReturn(emailSetting);

        when(mockConfiguration.getTemplate(anyString())).thenCallRealMethod();

        boolean emailSent = emailTemplateService.sendSoldEmail("CUC", EMAIL, "111");

        verify(mockEmailService, times(1))
                .sendMimeMessage(anyObject(), anyString(), anyString());

        assertTrue(emailSent);
    }

    @Test
    public void shouldNotSendSimpleSoldEmail() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        EmailSetting emailSetting = simpleEmailSetting();

        when(mockConfiguration.getTemplate(anyString())).thenCallRealMethod();

        when(mockEmailSettingService.getEmailSetting(anyString())).thenReturn(emailSetting);

        boolean emailSent = emailTemplateService.sendSoldEmail("CUC", EMAIL, "111");

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());

        assertFalse(emailSent);
    }

    @Test
    public void shouldNotSendEnhancedSoldEmail() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        EmailSetting emailSetting = enhancedEmailSetting();

        when(mockConfiguration.getTemplate(anyString())).thenCallRealMethod();

        when(mockEmailSettingService.getEmailSetting(anyString())).thenReturn(emailSetting);

        boolean emailSent = emailTemplateService.sendSoldEmail("CUC", EMAIL, "111");

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());

        assertFalse(emailSent);
    }

    @Test
    public void shouldNotSendSimpleRenewalEmail() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        EmailSetting emailSetting = simpleEmailSetting();

        when(mockConfiguration.getTemplate(anyString())).thenCallRealMethod();

        when(mockEmailSettingService.getEmailSetting(anyString())).thenReturn(emailSetting);

        boolean emailSent = emailTemplateService.sendRenewalEmail("CUC", EMAIL, "111");

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());

        assertFalse(emailSent);
    }

    @Test
    public void shouldNotSendEnhancedRenewalEmail() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        EmailSetting emailSetting = enhancedEmailSetting();

        when(mockConfiguration.getTemplate(anyString())).thenCallRealMethod();

        when(mockEmailSettingService.getEmailSetting(anyString())).thenReturn(emailSetting);

        boolean emailSent = emailTemplateService.sendRenewalEmail("CUC", EMAIL, "111");

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());

        assertFalse(emailSent);
    }

    @Test
    public void shouldNotSendSimpleLiveEmail() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        EmailSetting emailSetting = simpleEmailSetting();

        when(mockConfiguration.getTemplate(anyString())).thenCallRealMethod();

        when(mockEmailSettingService.getEmailSetting(anyString())).thenReturn(emailSetting);

        boolean emailSent = emailTemplateService.sendGoLiveEmail("CUC", EMAIL, "111");

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());

        assertFalse(emailSent);
    }

    @Test
    public void shouldNotSendEnhancedLiveEmail() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        EmailSetting emailSetting = enhancedEmailSetting();

        when(mockConfiguration.getTemplate(anyString())).thenCallRealMethod();

        when(mockEmailSettingService.getEmailSetting(anyString())).thenReturn(emailSetting);

        boolean emailSent = emailTemplateService.sendGoLiveEmail("CUC", EMAIL, "111");

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());

        assertFalse(emailSent);
    }

    @Test
    public void shouldNotSendSimpleObjectedEmail() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        EmailSetting emailSetting = simpleEmailSetting();

        when(mockConfiguration.getTemplate(anyString())).thenCallRealMethod();

        when(mockEmailSettingService.getEmailSetting(anyString())).thenReturn(emailSetting);

        boolean emailSent = emailTemplateService.sendObjectedEmail("CUC", EMAIL, "111");

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());

        assertFalse(emailSent);
    }

    @Test
    public void shouldNotSendEnhancedObjectedEmail() throws Exception {
        PowerMockito.mockStatic(FreeMarkerTemplateUtils.class);

        PowerMockito.when(FreeMarkerTemplateUtils.processTemplateIntoString(any(Template.class), any(String.class)))
                .thenReturn("<html></html>");

        EmailSetting emailSetting = enhancedEmailSetting();

        when(mockConfiguration.getTemplate(anyString())).thenCallRealMethod();

        when(mockEmailSettingService.getEmailSetting(anyString())).thenReturn(emailSetting);

        boolean emailSent = emailTemplateService.sendObjectedEmail("CUC", EMAIL, "111");

        verify(mockEmailService, times(0))
                .sendMimeMessage(anyObject(), anyString(), anyString());

        assertFalse(emailSent);
    }

    private EmailSetting simpleEmailSetting() {
        EmailSetting emailSetting = new EmailSetting();
        emailSetting.setId(2L);
        emailSetting.setEmailFormat("SIMPLE");
        emailSetting.setEmailType("SOLD");
        emailSetting.setEnabled(true);

        return emailSetting;
    }

    private EmailSetting enhancedEmailSetting() {
        EmailSetting emailSetting = new EmailSetting();
        emailSetting.setId(2L);
        emailSetting.setEmailFormat("ENHANCED");
        emailSetting.setEmailType("SOLD");
        emailSetting.setEnabled(true);

        return emailSetting;
    }

    private Customer aCustomer() {
        Customer customer = new Customer();
        customer.setId(12L);

        return customer;
    }

    private User aCreatedUser() {
        User user = new User();
        user.setId(999L);

        return user;
    }

    private User aTaggedUser() {
        User user = new User();
        user.setId(999L);

        return user;
    }
}
