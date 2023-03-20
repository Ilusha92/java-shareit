package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.dao.UserDao;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Override
    public User createUser(UserDto userDto) {
        return userDao.createUser(userDto);
    }

    @Override
    public User updateUser(long userId, UserDto userDto) {
        return userDao.updateUser(userId, userDto);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        userDao.deleteUserById(userId);
    }
    
}
