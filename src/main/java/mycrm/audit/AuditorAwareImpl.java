package mycrm.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import mycrm.models.LoggedInUser;
import mycrm.models.User;
import mycrm.services.UserService;

public class AuditorAwareImpl implements AuditorAware<User> {

	private final UserService userService;

	@Autowired
	public AuditorAwareImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public User getCurrentAuditor() {

		User user = userService.findById(
				((LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

		return user;
	}

}
