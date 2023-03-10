package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    private static Map<Integer, UserDto> userMap = new HashMap<>();
    private static int idCounter = 0;

    private static int generateId() {
        return ++idCounter;
    }

    private static void userValidate(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()){
            throw new RuntimeException();
        }
        if (userDto.getEmail() == null){
            throw new RuntimeException();
        }
        if (userMap.containsKey(userDto.getEmail())){
            throw new RuntimeException();
        }
    }

    @Override
    public User createUser(UserDto userDto) {
        userValidate(userDto);
        User user = new User();
        user.setId(generateId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    @Override
    public User updateUser(UserDto userDto) {
        if (userMap.containsKey(userDto.getEmail())){
            throw new RuntimeException();
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userList;
    }

    @Override
    public User getUserById(int id) {
        return userList.get(id);
    }

    @Override
    public void deleteUserById(int id) {

    }
}
