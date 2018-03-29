package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.repository.RoleRepository;
import com.hoffman.carpool.repository.UserRepository;
import com.hoffman.carpool.domain.entity.User;
import com.hoffman.carpool.domain.security.UserRole;
import com.hoffman.carpool.service.AccountService;
import com.hoffman.carpool.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean checkUserExists(String username, String email){
        if (checkUsernameExists(username) || checkEmailExists(email)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userRepository.findByUsername(user.getUsername());

        if (localUser != null) {
            LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            user.setDriverAccount(accountService.createDriverAccount(user));
            user.setRiderAccount(accountService.createRiderAccount(user));

            localUser = userRepository.save(user);
        }

        return localUser;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
