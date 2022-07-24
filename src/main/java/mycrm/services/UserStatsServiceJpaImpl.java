package mycrm.services;

import mycrm.models.UserStat;
import mycrm.repositories.UserStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class UserStatsServiceJpaImpl implements UserStatsService {

	private final UserStatRepository userStatsRepo;

	@Autowired
	public UserStatsServiceJpaImpl(UserStatRepository userStatsRepo) {
		this.userStatsRepo = userStatsRepo;
	}

	@Override
	public List<UserStat> findAll() {
		return this.userStatsRepo.findAll();
	}

	@Override
	public UserStat create(UserStat userStat) {
		return this.userStatsRepo.save(userStat);
	}

	@Override
	public void deleteById(Long id) {
		this.userStatsRepo.delete(id);
	}

}
