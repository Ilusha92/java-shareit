package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.CreateCommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
                return itemService.createItem(itemDto, ownerId);

    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @PathVariable Long itemId) {

        return itemService.updateItem(itemDto, ownerId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                     @RequestParam(defaultValue = "0")
                                     @Min(0) int from,
                                     @RequestParam(defaultValue = "20")
                                         @Min(0) int size) {
        return itemService.getAllItems(ownerId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable ("itemId") Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByRequest(@RequestParam String text,
                                            @RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(defaultValue = "0")
                                                @Min(0) int from,
                                            @RequestParam(defaultValue = "20")
                                                @Min(0) int size) {
        return itemService.findItemsByRequest(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CreateCommentDto createCommentDto,
                                    @NotNull @PathVariable Long itemId,
                                    @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createComment(createCommentDto, itemId, userId);
    }

}
