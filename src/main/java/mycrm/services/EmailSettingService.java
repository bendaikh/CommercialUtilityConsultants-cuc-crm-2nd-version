package mycrm.services;

import mycrm.models.EmailSetting;

import java.util.List;

public interface EmailSettingService {
    EmailSetting getEmailSetting(String emailType);

    List<EmailSetting> findAll();

    EmailSetting findById(Long id);

    EmailSetting save(EmailSetting emailSetting);
}
