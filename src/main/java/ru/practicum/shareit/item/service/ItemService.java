package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item createItem(ItemDto itemDto);
    Item updateItem(Item item);
    List<Item> getAllItems(Long userId);
    Item getItemById(Long id);
    List<Item> findItemsByRequest(String text);

}
