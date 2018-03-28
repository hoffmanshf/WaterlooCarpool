package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.entity.User;
import com.hoffman.carpool.domain.security.UserRole;

import java.util.Set;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);

    User createUser(User user, Set<UserRole> userRoles);

    User saveUser (User user);
}
