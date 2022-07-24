package mycrm.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import mycrm.models.Broker;
import mycrm.models.LoggedInUser;
import mycrm.models.Role;
import mycrm.models.User;
import mycrm.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void shouldLoadUserByUsernameWithOutBrokerActiveUser() {
        String username = "imran.miskeen";

        Role role = new Role();
        role.setId(5);
        role.setRole("LEADS");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(Long.valueOf(42));
        user.setUsername(username);
        user.setActive(1);
        user.setPasswordHash("XXXXXXXXXXXXXX");
        user.setRoles(roles);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));

        LoggedInUser loggedInUser = new LoggedInUser(username, "XXXXXXXXXX", false, false, false, false,
                grantedAuthorities, Long.valueOf(42), "Imran", "Miskeen", "imran@cec.com");

        when(userRepo.findByUsername(username)).thenReturn(user);

        UserDetails loadUserByUsername = userDetailsServiceImpl.loadUserByUsername(username);

        assertEquals(loggedInUser, loadUserByUsername);
    }

    @Test
    public void shouldLoadUserByUsernameWithBrokerActiveUser() {
        String username = "imran.miskeen";

        Role role = new Role();
        role.setId(5);
        role.setRole("LEADS");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Broker broker = new Broker();
        broker.setId(Long.valueOf(41));

        User user = new User();
        user.setId(Long.valueOf(42));
        user.setUsername(username);
        user.setActive(1);
        user.setPasswordHash("XXXXXXXXXXXXXX");
        user.setRoles(roles);
        user.setBroker(broker);
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran@cec.com");

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));

        LoggedInUser loggedInUser = new LoggedInUser(username, "XXXXXXXXXXXXXX", false, false, false, false,
                grantedAuthorities, Long.valueOf(42), "Imran", "Miskeen", "imran@cec.com");

        loggedInUser.setBroker(broker);

        when(userRepo.findByUsername(username)).thenReturn(user);

        LoggedInUser loadUserByUsername = (LoggedInUser) userDetailsServiceImpl.loadUserByUsername(username);

        assertEquals(loggedInUser, loadUserByUsername);
        assertEquals(loggedInUser.getId(), loadUserByUsername.getId());
        assertEquals(loggedInUser.getUsername(), loadUserByUsername.getUsername());
        assertEquals(loggedInUser.getPassword(), loadUserByUsername.getPassword());
        assertEquals(loggedInUser.getFirstName(), loadUserByUsername.getFirstName());
        assertEquals(loggedInUser.getLastName(), loadUserByUsername.getLastName());
        assertEquals(loggedInUser.getEmailAddress(), loadUserByUsername.getEmailAddress());
        assertEquals(loggedInUser.getBroker(), loadUserByUsername.getBroker());
    }

    @Test
    public void shouldLoadUserByUsernameWithOutBrokerWithInactiveUser() {
        String username = "imran.miskeen";

        Role role = new Role();
        role.setId(5);
        role.setRole("LEADS");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(Long.valueOf(42));
        user.setUsername(username);
        user.setActive(0);
        user.setPasswordHash("XXXXXXXXXXXXXX");
        user.setRoles(roles);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));

        LoggedInUser loggedInUser = new LoggedInUser(username, "XXXXXXXXXX", false, false, false, false,
                grantedAuthorities, Long.valueOf(42), "Imran", "Miskeen", "imran@cec.com");

        when(userRepo.findByUsername(username)).thenReturn(user);

        UserDetails loadUserByUsername = userDetailsServiceImpl.loadUserByUsername(username);

        assertEquals(loggedInUser, loadUserByUsername);
    }

}
