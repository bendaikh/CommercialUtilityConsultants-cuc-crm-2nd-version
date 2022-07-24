package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.Role;
import mycrm.models.User;
import mycrm.services.BrokerService;
import mycrm.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService mockUserService;

    @Mock
    private BrokerService mockBrokerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldReturnViewOfUsers() throws Exception {

        List<User> users = new ArrayList<>();
        users.add(new User());

        when(mockUserService.findAll()).thenReturn(users);

        mockMvc
                .perform(get("/admin/users/users"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("findAll", hasSize(1)))
                .andExpect(view().name("admin/users/users"));
    }

    @Test
    public void shouldReturnUserEditView() throws Exception {

        User user = new User();
        user.setId(Long.valueOf(32));
        List<Broker> brokers = new ArrayList<>();
        brokers.add(new Broker());

        when(mockUserService.findById(Long.valueOf(32))).thenReturn(user);
        when(mockBrokerService.findAll()).thenReturn(brokers);

        mockMvc
                .perform(get("/admin/users/edit/32"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", instanceOf(User.class)))
                .andExpect(model().attribute("brokers", hasSize(1)))
                .andExpect(view().name("admin/users/edituser"));
    }

    @Test
    public void shouldReturnNewUserView() throws Exception {

        List<Broker> brokers = new ArrayList<>();
        brokers.add(new Broker());

        when(mockBrokerService.findAll()).thenReturn(brokers);

        mockMvc
                .perform(get("/admin/users/newuser"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", instanceOf(User.class)))
                .andExpect(model().attribute("brokers", hasSize(1)))
                .andExpect(view().name("admin/users/newuser"));
    }

    @Test
    public void shouldReturnChangePasswordView() throws Exception {

        User user = new User();
        user.setId(Long.valueOf(32));

        when(mockUserService.findById(Long.valueOf(32))).thenReturn(user);

        mockMvc
                .perform(get("/admin/users/changepassword/32"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user))
                .andExpect(view().name("admin/users/changepassword"));
    }

    @Test
    public void shouldUpdatePassword() throws Exception {

        String passwordHash = "poihdkfjgnjngkdjfngkdjfngdhfnghdfn";
        String confirmPassword = "poihdkfjgnjngkdjfngkdjfngdhfnghdfn";

        User user = new User();
        user.setId(Long.valueOf(32));

        mockMvc
                .perform(post("/updatePassword").param("passwordHash", passwordHash).param("confirmPassword",
                        confirmPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/users"));
        
        verify(mockUserService, times(1)).updatePassword(any(User.class), any(String.class));
    }
    
    @Test
    public void shouldNotUpdatePassword() throws Exception {

        String passwordHash = "poihd";
        String confirmPassword = "poihdkfjgnjngkdjfngkdjfngdhfnghdfn";

        User user = new User();
        user.setId(Long.valueOf(32));

        mockMvc
                .perform(post("/updatePassword").param("passwordHash", passwordHash).param("confirmPassword",
                        confirmPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/users"));
        
        verify(mockUserService, times(0)).updatePassword(any(User.class), any(String.class));
    }

    @Test
    public void shouldCreateUser() throws Exception {

        User user = new User();
        user.setId(Long.valueOf(32));

        mockMvc
                .perform(post("/createUser")
                        .param("firstName", "Imran")
                        .param("username", "imran.miskeen")
                        .param("lastName", "Miskeen")
                        .param("email", "imran@imran.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/users"));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        Role role = new Role();
        role.setId(1);

        User user = new User();
        user.setId(Long.valueOf(32));
        user.setUsername("imran.miskeen1");
        user.setFirstName("Imran1");
        user.setLastName("Miskeen1");
        user.setEmail("imran1@imran.com");

        when(mockUserService.findById(Long.valueOf(32))).thenReturn(user);

        mockMvc
                .perform(post("/updateUser")
                        .param("id", "32")
                        .param("username", "imran.miskeen1")
                        .param("firstName", "Imran1")
                        .param("lastName", "Miskeen1")
                        .param("email", "imran1@imran.com")
                        .param("roles", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/users"));
        
        verify(mockUserService, times(1)).update(any(User.class));
        verify(mockUserService, times(1)).updateRole(any(User.class), any(int.class));
    }
    
    @Test
    public void shouldNotUpdateUser() throws Exception {

        User user = new User();
        user.setId(Long.valueOf(32));

        Role role = new Role();
        role.setId(1);

        when(mockUserService.findById(Long.valueOf(32))).thenReturn(user);

        mockMvc
                .perform(post("/updateUser")
                        .param("id", "42")
                        .param("firstName", "Imran1")
                        .param("username", "imran.miskeen1")
                        .param("lastName", "Miskeen1")
                        .param("email", "imran1@imran.com")
                        .param("roles", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/users"));
        
        verify(mockUserService, times(0)).update(any(User.class));
        verify(mockUserService, times(0)).updateRole(any(User.class), any(int.class));
    }

}
