package mycrm.services;

import mycrm.models.User;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User findById(Long id);
    User findUserByUsername(String username);
    User create(User user);
    void update(User user);
    User edit(User user);
    void deleteById(Long id);
	void updatePassword(User user, String passwordHash);
	void updateRole(User user, int role);

    List<User> findAllActiveUsers();

    List<User> findAllAdmin();
}
