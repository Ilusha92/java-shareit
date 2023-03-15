package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;

    }
    public static Item toItem(ItemDto itemDto, Long ownerId) {

        Item item = new Item();
        item.setId(null);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwnerId(ownerId);
        return item;

    }


//        public Item toModel(ItemDto itemDto, Long ownerId) {
//            return new Item(null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId);
//        }
//
//        public List<ItemDto> mapItemListToItemDtoList(List<Item> userItems) {
//            if (userItems.isEmpty()) {
//                return new ArrayList<>();
//            }
//
//            List<ItemDto> result = new ArrayList<>();
//            for (Item item : userItems) {
//                ItemDto itemDto = toDto(item);
//                result.add(itemDto);
//            }
//            return result;
//        }

}
