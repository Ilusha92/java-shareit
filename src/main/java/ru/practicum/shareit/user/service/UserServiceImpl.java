package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto, null);
        user = userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = patchUser(userDto, userId);
        user = userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    private User patchUser(UserDto userDto, long userId) {
        UserDto userDtoToUpdate = getUserById(userId);

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            userDtoToUpdate.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            userDtoToUpdate.setEmail(userDto.getEmail());
        }
        return UserMapper.toUser(userDtoToUpdate, userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toUserDtoList(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow());
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }
}
