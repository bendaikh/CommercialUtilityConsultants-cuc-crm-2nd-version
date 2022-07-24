package mycrm.services;

import mycrm.models.Broker;
import mycrm.models.Role;
import mycrm.models.User;
import mycrm.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceJpaImplTest {

    @InjectMocks
    private UserServiceJpaImpl userServiceJpaImpl;

    @Mock
    private User mockedUser;

    @Mock
    private UserRepository mockedUserRepository;
    
    @Mock
    private Role mockedRole;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void shouldFindAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());

        when(mockedUserRepository.findAll()).thenReturn(userList);

        List<User> findAll = userServiceJpaImpl.findAll();

        assertEquals(userList, findAll);
    }

    @Test
    public void shouldFindUserByIdSuperAdmin() {
        
        Role role = new Role();
        role.setId(2);
        role.setRole("SUPERADMIN");
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        
        User user = new User();
        user.setId(Long.valueOf(5));
        user.setRoles(roles);

        when(mockedUserRepository.findOne(Long.valueOf(5))).thenReturn(user);

        User findById = userServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(user, findById);
        assertTrue(findById.isSuperAdmin());
        assertFalse(findById.isAdmin());
        assertFalse(findById.isExternalBroker());
        assertFalse(findById.isLeads());
        assertFalse(findById.isBroker());
        assertTrue(findById.getRoles().contains(role));
    }
    
    @Test
    public void shouldFindUserByIdAdmin() {
        
        Role role = new Role();
        role.setId(1);
        role.setRole("ADMIN");
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        
        User user = new User();
        user.setId(Long.valueOf(5));
        user.setRoles(roles);

        when(mockedUserRepository.findOne(Long.valueOf(5))).thenReturn(user);

        User findById = userServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(user, findById);
        assertFalse(findById.isSuperAdmin());
        assertTrue(findById.isAdmin());
        assertFalse(findById.isExternalBroker());
        assertFalse(findById.isLeads());
        assertFalse(findById.isBroker());
    }
    
    @Test
    public void shouldFindUserByIdExternalBroker() {
        
        Role role = new Role();
        role.setId(4);
        role.setRole("EXTERNAL_BROKER");
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        
        User user = new User();
        user.setId(Long.valueOf(5));
        user.setRoles(roles);

        when(mockedUserRepository.findOne(Long.valueOf(5))).thenReturn(user);

        User findById = userServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(user, findById);
        assertFalse(findById.isSuperAdmin());
        assertFalse(findById.isAdmin());
        assertTrue(findById.isExternalBroker());
        assertFalse(findById.isLeads());
        assertFalse(findById.isBroker());
    }
    
    @Test
    public void shouldFindUserByIdLeads() {
        
        Role role = new Role();
        role.setId(5);
        role.setRole("LEADS");
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        
        User user = new User();
        user.setId(Long.valueOf(5));
        user.setRoles(roles);

        when(mockedUserRepository.findOne(Long.valueOf(5))).thenReturn(user);

        User findById = userServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(user, findById);
        assertFalse(findById.isSuperAdmin());
        assertFalse(findById.isAdmin());
        assertFalse(findById.isExternalBroker());
        assertTrue(findById.isLeads());
        assertFalse(findById.isBroker());
    }
    
    @Test
    public void shouldFindUserByIdBroker() {
        Broker broker = new Broker();
        broker.setId(999l);
        
        Role role = new Role();
        role.setId(3);
        role.setRole("BROKER");
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        
        User user = new User();
        user.setId(Long.valueOf(5));
        user.setRoles(roles);
        user.setBroker(broker);
        
        when(mockedUserRepository.findOne(Long.valueOf(5))).thenReturn(user);

        User findById = userServiceJpaImpl.findById(Long.valueOf(5));

        assertEquals(user, findById);
        assertFalse(findById.isSuperAdmin());
        assertFalse(findById.isAdmin());
        assertFalse(findById.isExternalBroker());
        assertFalse(findById.isLeads());
        assertTrue(findById.isBroker());
    }

    @Test
    public void shouldSaveUser() {
        User user = new User();
        user.setId(Long.valueOf(5));
        user.setPasswordHash("##########");

        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("##########");
        when(mockedUserRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userServiceJpaImpl.create(user);

        assertEquals(user, savedUser);
    }

    @Test
    public void shouldEditAndSaveUserSuperAdmin() {
        Broker broker = new Broker();
        broker.setId(Long.valueOf(54));
        
        Role role = new Role();
        role.setId(2);
        role.setRole("SUPERADMIN");
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
                
        User user = new User();
        user.setId(Long.valueOf(5));
        user.setVersion(1);
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran@cec.com");
        user.setActive(1);
        user.setBroker(broker);
        user.setRoles(roles);
        user.setBrokers(new HashSet<>());
        user.setCustomers(new HashSet<>());
        user.setBrokerNotes(new HashSet<>());
        user.setCustomerNotes(new HashSet<>());
        user.setSuppliers(new HashSet<>());
        user.setUserStats(new HashSet<>());

        when(mockedUserRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userServiceJpaImpl.edit(user);

        assertEquals(user, savedUser);
        assertEquals(user.getVersion(), savedUser.getVersion());
        assertEquals(user.getRoles(), savedUser.getRoles());
        assertEquals(user.getBrokers(), savedUser.getBrokers());
        assertEquals(user.getCustomers(), savedUser.getCustomers());
        assertEquals(user.getBrokerNotes(), savedUser.getBrokerNotes());
        assertEquals(user.getCustomerNotes(), savedUser.getCustomerNotes());
        assertEquals(user.getSuppliers(), savedUser.getSuppliers());
        assertEquals(user.getUserStats(), savedUser.getUserStats());
        assertTrue(user.isSuperAdmin());
    }

    @Test
    public void shouldDeleteUser() {
        userServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedUserRepository, times(1)).delete(Long.valueOf(541));
    }

    @Test
    public void shouldFindUserByUsername() {
        User user = new User();
        user.setId(Long.valueOf(654));
        user.setUsername("imran.miskeen");

        when(mockedUserRepository.findByUsername("imran.miskeen")).thenReturn(user);

        User findUserByUsername = userServiceJpaImpl.findUserByUsername("imran.miskeen");

        assertEquals(user, findUserByUsername);
    }

    @Test
    public void shouldUpdateUser() {

        Broker broker = new Broker();
        broker.setId(Long.valueOf(54));
        
        Role role = new Role();
        role.setId(2);
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(Long.valueOf(23));
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran@cec.com");
        user.setActive(1);
        user.setBroker(broker);
        user.setRoles(roles);
        user.setDiallerAgentReference("123");

        userServiceJpaImpl.update(user);

        verify(mockedUserRepository, times(1)).updateUser(user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getDiallerAgentReference(), user.getEmail(), user.getActive(), user.getBroker(), user.getId());
    }

    @Test
    public void shouldUpdateRole() {

        Role role = new Role();
        role.setId(2);
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(Long.valueOf(23));
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setEmail("imran@cec.com");        
        user.setRoles(roles);
        user.setActive(1);

        userServiceJpaImpl.updateRole(user, 2);

        verify(mockedUserRepository, times(1)).updateRole(any(Role.class), any(Long.class));
    }

    @Test
    public void shouldUpdatePassword() {
        User user = new User();
        user.setId(Long.valueOf(23));
        user.setUsername("imran.miskeen");
        user.setFirstName("Imran");
        user.setLastName("Miskeen");
        user.setPasswordHash("XXX");
        user.setEmail("imran@cec.com");        
        user.setActive(1);
        
        when(bCryptPasswordEncoder.encode("111")).thenReturn("XXX");        
        
        userServiceJpaImpl.updatePassword(user, "111");
        
        verify(mockedUserRepository, times(1)).updatePassword("XXX", Long.valueOf(23));
    }

}
