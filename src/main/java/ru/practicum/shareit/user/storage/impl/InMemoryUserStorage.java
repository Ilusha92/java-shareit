package ru.practicum.shareit.user.storage.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exceptions.EmailValidationException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.dao.UserDao;

import java.util.*;

@Repository
public class InMemoryUserStorage implements UserDao {

    private final Map<Long, User> userMap = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private long idCounter = 1;

    private void checkEmailInEmailsList(String email) {
        if (emails.contains(email)) throw new EmailValidationException("This email is registered");
    }

    private void tryRefreshUserEmail(String oldEmail, String newEmail) {
        emails.remove(oldEmail);
        if (emails.contains(newEmail)) {
            emails.add(oldEmail);
            throw new EmailValidationException("This email is registered");
        }
        emails.add(newEmail);
    }

    @Override
    public User createUser(User user) {
        checkEmailInEmailsList(user.getEmail());
        user.setId(idCounter++);
        userMap.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(long id, User user) {
        if (!userMap.containsKey(id)) {
            throw new UserNotFoundException("User not found");
        }
        User userToUpdate = userMap.get(id);
        String userToUpdateEmail = userMap.get(id).getEmail();
        String newUserEmail = user.getEmail();

        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }
        if (newUserEmail != null && !userToUpdateEmail.equals(newUserEmail)) {
            tryRefreshUserEmail(userToUpdateEmail, newUserEmail);
            userToUpdate.setEmail(newUserEmail);
        }
        return userToUpdate;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUserById(long id) {
        if (!userMap.containsKey(id)) {
            throw new UserNotFoundException("User not found");
        }
        return userMap.get(id);
    }

    @Override
    public void deleteUserById(long id) {
        if (!userMap.containsKey(id)) {
            throw new UserNotFoundException("User not found");
        }
        emails.remove(userMap.get(id).getEmail());
        userMap.remove(id);
    }
}
