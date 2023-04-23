package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(long id, UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(long id);

    void deleteUserById(long id);
}
