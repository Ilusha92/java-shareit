package ru.practicum.shareit.item.storage.dao;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemDao {

    Item createItem(ItemDto itemDto);
    Item updateItem(Item item);
    List<Item> getAllItems(Long userId);
    Item getItemById(Long id);
    List<Item> findItemsByRequest(String text);

}
