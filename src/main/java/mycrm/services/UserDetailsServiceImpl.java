package mycrm.services;

import mycrm.models.LoggedInUser;
import mycrm.models.Role;
import mycrm.models.User;
import mycrm.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static Logger logger = LogManager.getLogger();

    private final UserRepository userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoggedInUser loggedInUser = null;

        User user = userRepo.findByUsername(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        loggedInUser = new LoggedInUser(username, user.getPasswordHash(), (user.getActive() == 1 ? true : false), true,
                true, true, grantedAuthorities, user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

        if (user.getBroker() != null) {
            loggedInUser.setBroker(user.getBroker());
        }

        // logger.info("Logged In user,loggedInUser={}", loggedInUser);
        // for some reason deleting the line below breaks everything lol
        if (loggedInUser.getBroker() != null) {
            logger.info("Logged In user is broker={}", loggedInUser.getBroker().getId());
        }

        // return new
        // org.springframework.security.core.userdetails.User(user.getUsername(),
        // user.getPasswordHash(),grantedAuthorities);
        return loggedInUser;
    }

}
