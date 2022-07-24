package mycrm.configuration;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {
	
//	@Autowired
//	private UsersService usersService;
//	
//	@Autowired
//	private UserStatsService userStatsService;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		
		//UPON LOGGING IN TELL ME WHAT TO DO HERE :-)
//		UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
//		    	
//    	User user = usersService.findUserByUsername(userDetails.getUsername());    	   
//    	
//    	userStatsService.create(new UserStats(userDetails.getUsername(), user));
		
	}

}
