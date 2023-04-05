package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.CreateCommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemdto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long ownerId, Long itemId);

    CommentDto createComment(CreateCommentDto dto, Long itemId, Long userId);

    List<ItemDto> getAllItems(Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> findItemsByRequest(String text, Long userId);
}
