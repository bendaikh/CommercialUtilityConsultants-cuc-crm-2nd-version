package mycrm.services;

import mycrm.models.Role;
import mycrm.models.User;
import mycrm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class UserServiceJpaImpl implements UserService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceJpaImpl(UserRepository userRepo,
                              BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> findAll() {
        return this.userRepo.findAll();
    }

    @Override
    public User findById(Long id) {
        return this.userRepo.findOne(id);
    }

    @Override
    public User create(User user) {
        user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));
        return this.userRepo.save(user);
    }

    @Override
    public void update(User user) {

        this.userRepo.updateUser(user.getUsername(), user.getFirstName(), user.getLastName(), user.getDiallerAgentReference(), user.getEmail(),
                user.getActive(), user.getBroker(), user.getId());
    }

    @Override
    public void updateRole(User user, int role) {
        Role updatedRole = new Role();
        updatedRole.setId(role);

        this.userRepo.updateRole(updatedRole, user.getId());
    }

    @Override
    public List<User> findAllActiveUsers() {
        return this.userRepo.findAllActiveUsers();
    }

    @Override
    public List<User> findAllAdmin() {
        List<User> adminList = new ArrayList<>();
        this.userRepo.findAll().forEach(user -> user.getRoles().forEach(role -> {
            if (role.getRole().equals("ADMIN")) {
                adminList.add(user);
            }
        }));
        return adminList;
    }

    @Override
    public User edit(User user) {
        return this.userRepo.save(user);
    }

    @Override
    public void deleteById(Long id) {
        this.userRepo.delete(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public void updatePassword(User user, String passwordHash) {
        this.userRepo.updatePassword(bCryptPasswordEncoder.encode(passwordHash), user.getId());
    }

}
