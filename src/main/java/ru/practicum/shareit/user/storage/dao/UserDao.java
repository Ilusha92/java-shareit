package ru.practicum.shareit.user.storage.dao;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserDao {

    User createUser(UserDto userDto);

    User updateUser(long id, UserDto userDto);

    List<User> getAllUsers();

    User getUserById(long id);
    
    void deleteUserById(long id);
}
