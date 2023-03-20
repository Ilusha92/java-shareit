package ru.practicum.shareit.item.storage.dao;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemDao {

    Item createItem(Item item);

    Item updateItem(Item item);

    List<Item> getAllItems(Long userId);

    Item getItemById(Long id);

    List<Item> findItemsByRequest(String text);
    
}
