package mycrm.services;

import mycrm.models.UserStat;

import java.util.List;

public interface UserStatsService {

    List<UserStat> findAll();

    UserStat create(UserStat userStat);

    void deleteById(Long id);

}
