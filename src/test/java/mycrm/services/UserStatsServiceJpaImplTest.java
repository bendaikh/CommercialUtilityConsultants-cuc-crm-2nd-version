package mycrm.services;

import mycrm.models.User;
import mycrm.models.UserStat;
import mycrm.repositories.UserStatRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserStatsServiceJpaImplTest {

    @InjectMocks
    private UserStatsServiceJpaImpl userStatsServiceJpaImpl;

    @Mock
    private UserStat mockedUserStat;

    @Mock
    private UserStatRepository mockedUserStatRepository;

    @Test
    public void shouldReturnListOfAllUserStats() {
        
        User user = new User();
        user.setId(Long.valueOf(4));

        List<UserStat> userStats = new ArrayList<>();
        userStats.add(new UserStat("imran.miskeen", user, "192.168.0.1", "/requestedURL", "sessionid", "message"));

        when(mockedUserStatRepository.findAll()).thenReturn(userStats);

        List<UserStat> findAll = userStatsServiceJpaImpl.findAll();

        assertEquals(userStats, findAll);
    }

    @Test
    public void shouldCreateUserStats() {

        User user = new User();
        user.setId(Long.valueOf(4));

        UserStat userStat = new UserStat();
        userStat.setId(Long.valueOf(55));
        userStat.setUsername("imran.miskeen");
        userStat.setUser(user);
        userStat.setIpAddress("192.168.0.1");
        userStat.setRequestedURL("/requestedURL");
        userStat.setSessionId("sessionid"); 
        userStat.setMessage("message");
        userStat.setLoginDate(new Date());

        when(mockedUserStatRepository.save(any(UserStat.class))).thenReturn(userStat);

        UserStat createdUserStat = userStatsServiceJpaImpl.create(userStat);

        assertEquals(userStat, createdUserStat);
        assertEquals(userStat.getId(), createdUserStat.getId());
        assertEquals(userStat.getUsername(), createdUserStat.getUsername());
        assertEquals(userStat.getUser(), createdUserStat.getUser());
        assertEquals(userStat.getIpAddress(), createdUserStat.getIpAddress());
        assertEquals(userStat.getRequestedURL(), createdUserStat.getRequestedURL());
        assertEquals(userStat.getSessionId(), createdUserStat.getSessionId());
        assertEquals(userStat.getMessage(), createdUserStat.getMessage());
        assertNotNull(userStat.getLoginDate());
    }
    
    @Test
    public void shouldCreateUserStatsWithUsernameAndUser() {

        User user = new User();
        user.setId(Long.valueOf(4));

        UserStat userStat = new UserStat("imran.miskeen", user);

        when(mockedUserStatRepository.save(any(UserStat.class))).thenReturn(userStat);

        UserStat createdUserStat = userStatsServiceJpaImpl.create(userStat);

        assertEquals(userStat, createdUserStat);
        assertEquals(userStat.getUsername(), createdUserStat.getUsername());
        assertEquals(userStat.getUser(), createdUserStat.getUser());
        assertEquals(userStat.toString(), createdUserStat.toString());
    }

    @Test
    public void shouldDeleteUserStat() {
        userStatsServiceJpaImpl.deleteById(Long.valueOf(541));

        verify(mockedUserStatRepository, times(1)).delete(Long.valueOf(541));
    }

}
