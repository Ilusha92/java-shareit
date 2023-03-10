package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserDto userDto);
    User updateUser(UserDto userDto);
    List<User> getAllUsers();
    User getUserById(int id);
    void deleteUserById(int id);

}
