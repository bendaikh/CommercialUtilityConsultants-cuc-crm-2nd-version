package mycrm.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import mycrm.audit.AuditorAwareImpl;
import mycrm.models.User;
import mycrm.services.UserService;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {

	@Autowired
	private UserService userService;

	@Bean
	public AuditorAware<User> auditorAware() {
		return new AuditorAwareImpl(userService);
	}
}
