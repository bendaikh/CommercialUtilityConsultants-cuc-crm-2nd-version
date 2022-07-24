package mycrm.repositories;

import mycrm.models.EmailSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSettingRepository extends JpaRepository<EmailSetting, Long> {
    EmailSetting findByEmailType(String emailType);
}
