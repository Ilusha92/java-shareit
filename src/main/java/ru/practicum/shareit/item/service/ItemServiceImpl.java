package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exceptions.OwnerNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.dao.ItemDao;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.dao.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService{

    private ItemDao itemDao;
    private UserDao userDao;

    @Override
    public Item createItem(Item item) {
        boolean ownerExists = isOwnerExists(item.getOwnerId());
        if (!ownerExists){
            throw new OwnerNotFoundException("owner not found");
        }
        return itemDao.createItem(item);
    }

    private boolean isOwnerExists(long ownerId) {
        List<User> users = userDao.getAllUsers();
        List<User> result = users.stream().filter(user -> user.getId() == ownerId).collect(Collectors.toList());
        return result.size() > 0;
//        return userDao.getUserById(ownerId) > 0;
    }

    @Override
    public Item updateItem(Item item) {
        return itemDao.updateItem(item);
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return itemDao.getAllItems(userId);
    }

    @Override
    public Item getItemById(Long id) {
        return itemDao.getItemById(id);
    }

    @Override
    public List<Item> findItemsByRequest(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemDao.findItemsByRequest(text);
    }
}
