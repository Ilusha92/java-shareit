package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.*;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    public static final Sort SORT = Sort.by("created").descending();

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemRequestResponseDto createRequest(ItemRequestIncomingDto dto, Long userId) {
        checkIfUserExists(userId);
        ItemRequest itemRequest  = ItemRequestMapper.toItemRequest(dto, userId);
        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestResponseDto(itemRequest);
    }

    @Override
    public List<ItemRequestInItemDto> findAllByUserId(Long userId) {
        checkIfUserExists(userId);
        List<ItemRequest> requests = itemRequestRepository.findRequestByRequestorIdOrderByCreatedDesc(userId);
        return ItemRequestMapper.toItemRequestWithItemsDtoList(requests, itemRepository);
    }

    @Override
    public List<ItemRequestInItemDto> findAll(int from, int size, Long userId) {
        checkIfUserExists(userId);
        Pageable pageable = PageRequest.of(from / size, size, SORT);
        Page<ItemRequest> requests = itemRequestRepository.findAll(userId, pageable);
        return ItemRequestMapper.toItemRequestWithItemsDtoList(requests, itemRepository);
    }

    @Override
    public ItemRequestInItemDto findById(Long requestId, Long userId) {
        checkIfUserExists(userId);
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow();
        List<Item> items = itemRepository.findAllByRequestId(requestId);
        return ItemRequestMapper.toItemRequestWithItemsDto(request, items);
    }

    private void checkIfUserExists(Long userId) {
        userRepository.findById(userId).orElseThrow();
    }
}
