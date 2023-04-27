package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequestInItemDto;
import ru.practicum.shareit.request.model.ItemRequestIncomingDto;
import ru.practicum.shareit.request.model.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestResponseDto createRequest(ItemRequestIncomingDto dto, Long userId);

    List<ItemRequestInItemDto> findAllByUserId(Long userId);

    List<ItemRequestInItemDto> findAll(int from, int size, Long userId);

    ItemRequestInItemDto findById(Long requestId, Long userId);
}