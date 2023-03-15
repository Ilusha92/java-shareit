package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;
    private ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public Item createItem(@Valid @RequestBody ItemDto itemDto,
                                  @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        Item item = itemMapper.toItem(itemDto, ownerId);
        return itemService.createItem(itemMapper.toItemDto(item));

    }

    @PatchMapping("/{id}")
    public Item updateItem(@Valid @NotNull @RequestBody ItemDto itemDto,
                                           @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                           @PathVariable Long itemId) {
        Item item = itemMapper.toItem(itemDto, ownerId);
        item.setId(itemId);
        return itemService.updateItem(item);
    }

    @GetMapping
    public List<User> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public User getItemById(@NotNull @PathVariable int id) {
        return itemService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@NotNull @PathVariable int id) {
        itemService.deleteUserById(id);
    }

}
