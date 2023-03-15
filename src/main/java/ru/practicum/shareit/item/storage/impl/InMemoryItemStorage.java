package ru.practicum.shareit.item.storage.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.storage.dao.ItemDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryItemStorage implements ItemDao {

    private final Map<Long, Item> itemMap = new HashMap<>();
    private long idCounter = 1;

    @Override
    public Item createItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(idCounter++);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        long itemId = item.getId();
        if (!itemMap.containsKey(itemId)) {
            throw new ItemNotFoundException("this item not found in storage");
        }
        Item itemFromStorage = itemMap.get(itemId);
        itemFromStorage.setName(item.getName());
        itemFromStorage.setDescription(item.getDescription());
        itemFromStorage.setAvailable(item.getAvailable());
        return itemFromStorage;
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        List<Item> result = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if (item.getOwnerId().equals(userId))
                result.add(item);
        }
        return result;
    }

    @Override
    public Item getItemById(Long id) {
        return itemMap.get(id);
    }

    @Override
    public List<Item> findItemsByRequest(String text) {
        List<Item> result = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if (item.getName().equals(text))
                result.add(item);
        }
        return result;
    }
}
