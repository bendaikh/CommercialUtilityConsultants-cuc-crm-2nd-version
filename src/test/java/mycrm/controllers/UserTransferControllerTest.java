package mycrm.controllers;

import mycrm.models.User;
import mycrm.models.UserTaggedNotesPackage;
import mycrm.services.UserService;
import mycrm.services.UserTransferService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UserTransferControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserTransferController controller;

    @Mock
    private UserService mockUserService;

    @Mock
    private UserTransferService mockUserTransferService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldReturnUserTransferPortalPage() throws Exception {
        User selectedUser = new User();
        selectedUser.setId(2L);

        when(mockUserService.findById(any(Long.class))).thenReturn(selectedUser);

        mockMvc.perform(get("/admin/users/transfer-portal/55"))
                .andExpect(view().name("admin/users/transfer-portal/transfer-portal"))
                .andExpect(model().attribute("selectedUser", selectedUser))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUserNotesTransferPage() throws Exception {
        User selectedUser = new User();
        selectedUser.setId(2L);

        User dumbUser = new User();
        dumbUser.setId(23L);

        List<User> users = new ArrayList<>();
        users.add(dumbUser);

        UserTaggedNotesPackage notes = UserTaggedNotesPackage
                .builder()
                .numberOfTaggedNotes(4)
                .numberOfTaggedChildNotes(2)
                .build();

        when(mockUserService.findById(any(Long.class))).thenReturn(selectedUser);
        when(mockUserTransferService.findUserTaggedNotes(any(User.class))).thenReturn(notes);
        when(mockUserService.findAll()).thenReturn(users);

        mockMvc.perform(get("/admin/users/transfer-portal/user-notes/55"))
                .andExpect(view().name("admin/users/transfer-portal/user-notes"))
                .andExpect(model().attribute("selectedUser", selectedUser))
                .andExpect(model().attribute("notes", notes))
                .andExpect(model().attribute("users", users))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnSuccess() throws Exception {
        when(mockUserTransferService.transferUserNotes(anyObject())).thenReturn(true);

        mockMvc.perform(post("/admin/users/transfer-portal/transfer-user-notes"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/transfer-portal/confirmation?success=true"));
    }

    @Test
    public void shouldReturnFailure() throws Exception {
        when(mockUserTransferService.transferUserNotes(anyObject())).thenReturn(false);

        mockMvc.perform(post("/admin/users/transfer-portal/transfer-user-notes"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/transfer-portal/confirmation?success=false"));
    }

    @Test
    public void shouldShowSuccessfulConfirmationPage() throws Exception {
        mockMvc.perform(get("/admin/users/transfer-portal/confirmation?success=true"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/transfer-portal/confirmation"))
                .andExpect(model().attribute("message", "Updated"));
    }

    @Test
    public void shouldShowFailedConfirmationPage() throws Exception {
        mockMvc.perform(get("/admin/users/transfer-portal/confirmation?success=false"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users/transfer-portal/confirmation"))
                .andExpect(model().attribute("message", "Failed"));
    }
}
