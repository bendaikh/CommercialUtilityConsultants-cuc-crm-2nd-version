package mycrm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mycrm.models.UserStat;

@Repository
public interface UserStatRepository extends JpaRepository<UserStat, Long> {
       
}
