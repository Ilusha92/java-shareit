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
    public List<Item> getAllItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getAllItems(ownerId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable ("itemId") Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<Item> findItemsByRequest(@RequestParam String text) {
        return itemService.findItemsByRequest(text);
    }

}
