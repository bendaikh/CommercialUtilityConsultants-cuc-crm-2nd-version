package mycrm.services;

import mycrm.models.EmailSetting;
import mycrm.repositories.EmailSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class EmailSettingServiceImpl implements EmailSettingService {

    private final EmailSettingRepository emailSettingRepository;

    @Autowired
    public EmailSettingServiceImpl(EmailSettingRepository emailSettingRepository) {
        this.emailSettingRepository = emailSettingRepository;
    }

    @Override
    public EmailSetting getEmailSetting(String emailType) {
        return this.emailSettingRepository.findByEmailType(emailType);
    }

    @Override
    public List<EmailSetting> findAll() {
        return this.emailSettingRepository.findAll();
    }

    @Override
    public EmailSetting findById(Long id) {
        return this.emailSettingRepository.findOne(id);
    }

    @Override
    public EmailSetting save(EmailSetting emailSetting) {
        return this.emailSettingRepository.save(emailSetting);
    }
}
