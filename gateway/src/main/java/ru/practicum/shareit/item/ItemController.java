package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid
                              @RequestBody ItemDto itemDto,
                              @NotNull(message = ("itemID is null"))
                              @Min(1)
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.createItem(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid
                                            @RequestBody CreateCommentDto commentDto,
                                            @NotNull(message = ("itemID is null"))
                                            @Min(1)
                                            @PathVariable Long itemId,
                                            @NotNull(message = ("userID is null"))
                                            @Min(1)
                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.createComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
                              @RequestBody ItemDto itemDto,
                              @NotNull(message = "itemID is null")
                              @Min(1)
                              @PathVariable Long itemId,
                              @NotNull(message = "userID is null")
                              @Min(1)
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@NotNull(message = "itemID is null")
                                @Min(1)
                                @PathVariable Long itemId,
                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.findItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItems(@NotNull(message = "userID is null")
                                      @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestParam(defaultValue = "0")
                                      @Min(1) int from,
                                      @RequestParam(defaultValue = "20")
                                      @Min(1) int size) {
        return itemClient.findAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemsByRequest(@RequestParam String text,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "0")
                                                     @Min(1) int from,
                                                     @RequestParam(defaultValue = "20")
                                                     @Min(1) int size) {
        return itemClient.findItemsByRequest(text, userId, from, size);
    }
}