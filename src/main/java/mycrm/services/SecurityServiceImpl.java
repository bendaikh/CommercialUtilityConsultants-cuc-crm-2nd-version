package mycrm.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Override
    public String findLoggedInUsername() {

        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();

        if (userDetails instanceof UserDetails) {
            logger.info(String.format("Login %s successfully!", ((UserDetails) userDetails).getUsername()));
            return ((UserDetails) userDetails).getUsername();
        }

        return null;
    }

    @Override
    public void autologin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails != null) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

//        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug(String.format("Auto login %s successfully!", username));
//        }
        }
    }
}
