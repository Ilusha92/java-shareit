package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserDto userDto);

    User updateUser(long id, UserDto userDto);

    List<User> getAllUsers();

    User getUserById(long id);

    void deleteUserById(long id);
}
