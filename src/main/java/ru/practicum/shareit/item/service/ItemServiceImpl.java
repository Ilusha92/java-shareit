package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.dao.ItemDao;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService{

    private ItemDao itemDao;

    @Override
    public Item createItem(ItemDto itemDto) {
        return itemDao.createItem(itemDto);
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
        return itemDao.findItemsByRequest(text);
    }
}
