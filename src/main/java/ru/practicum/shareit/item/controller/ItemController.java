package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;
    private ItemMapper itemMapper;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        Item item = itemMapper.toItem(itemDto, ownerId);
        return itemMapper.toItemDto(itemService.createItem(item));

    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @PathVariable Long itemId) {
        Item item = itemMapper.toItem(itemDto, ownerId);
        item.setId(itemId);
        return itemMapper.toItemDto(itemService.updateItem(item));
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        List<Item> ownerItems = itemService.getAllItems(ownerId);
        return itemMapper.toItemDtoList(ownerItems);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable ("itemId") Long itemId) {
        Item item = itemService.getItemById(itemId);
        return itemMapper.toItemDto(item);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByRequest(@RequestParam String text) {
        List<Item> searchResult = itemService.findItemsByRequest(text);
        return itemMapper.toItemDtoList(searchResult);
    }

}
