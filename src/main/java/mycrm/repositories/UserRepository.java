package mycrm.repositories;

import mycrm.models.Broker;
import mycrm.models.Role;
import mycrm.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u ORDER BY u.dateCreated DESC")
	List<User> findAllUsers(Pageable pageable);

	User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.active='1'")
    List<User> findAllActiveUsers();

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE User SET username=(:username), firstName=(:firstName), lastName=(:lastName), diallerAgentReference=(:diallerAgentReference), email=(:email), active=(:active), " +
			"broker=(:broker) " +
			"where id=:id")
	void updateUser(@Param("username") String username, @Param("firstName") String firstName,
					@Param("lastName") String lastName, @Param("diallerAgentReference") String diallerAgentReference, @Param("email") String email, @Param("active") int active,
					@Param("broker") Broker broker, @Param("id") Long id);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE user_role SET role_id=:role_id where user_id=:id", nativeQuery = true)
	void updateRole(@Param("role_id") Role role, @Param("id") Long id);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE User SET passwordHash=(:passwordHash) where id=:id")
	void updatePassword(@Param("passwordHash") String passwordHash, @Param("id") Long id);

}
