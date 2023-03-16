package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item toItem(ItemDto itemDto, Long ownerId) {
            return new Item(null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId);
    }

    public List<ItemDto> toItemDtoList(List<Item> items) {
        if (items.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = toItemDto(item);
            result.add(itemDto);
        }
        return result;
    }
}
