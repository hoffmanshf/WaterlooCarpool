package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.dao.UserDao;
import com.hoffman.carpool.domain.User;
import com.hoffman.carpool.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;
    @Override
    public User findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return this.userDao.findByEmail(email);
    }

    @Override
    public boolean checkUserExists(String username, String email){
        if (checkUsernameExists(username) || checkEmailExists(email)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        }
        return false;
    }

    @Override
    public void save(User user) {
        this.userDao.save(user);
    }
}
