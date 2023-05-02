package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestInItemDto;
import ru.practicum.shareit.request.model.ItemRequestIncomingDto;
import ru.practicum.shareit.request.model.ItemRequestResponseDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemRequestServiceTest {

    public static final long ID = 1L;
    public static final int FROM_VALUE = 0;
    public static final int SIZE_VALUE = 20;
    public static final LocalDateTime CREATED_DATE = LocalDateTime.now();

    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private ItemRequestService requestService;
    private ItemRequestRepository requestRepository;

    private User user;
    private ItemRequest request;

    @BeforeEach
    public void beforeEach() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        requestRepository = mock(ItemRequestRepository.class);
        requestService = new ItemRequestServiceImpl(
                userRepository,
                itemRepository,
                requestRepository);

        request = new ItemRequest(ID, "description", ID, CREATED_DATE);
        user = new User(ID, "name", "user@emali.com");
    }

    @Test
    public void createRequestTest() {
        ItemRequestIncomingDto inputDto = new ItemRequestIncomingDto(request.getDescription());

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(request);

        ItemRequestResponseDto responseDto = requestService.createRequest(inputDto, ID);

        assertNotNull(responseDto);
        assertEquals(ID, responseDto.getId());
        assertEquals(inputDto.getDescription(), responseDto.getDescription());
    }

    @Test
    void findAllByUserIdTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(requestRepository
                .findRequestByRequestorIdOrderByCreatedDesc(any(Long.class)))
                .thenReturn(new ArrayList<>());

        when(itemRepository.findAllByRequestId(any(Long.class)))
                .thenReturn(new ArrayList<>());

        List<ItemRequestInItemDto> result = requestService.findAllByUserId(ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(requestRepository.findAll(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        when(itemRepository.findAllByRequestId(any(Long.class)))
                .thenReturn(new ArrayList<>());

        List<ItemRequestInItemDto> result = requestService.findAll(FROM_VALUE, SIZE_VALUE, ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(requestRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(request));

        when(itemRepository.findAllByRequestId(any(Long.class)))
                .thenReturn(new ArrayList<>());


        ItemRequestInItemDto result = requestService.findById(ID, ID);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }
}
