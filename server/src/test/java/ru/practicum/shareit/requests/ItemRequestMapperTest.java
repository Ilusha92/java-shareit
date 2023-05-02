package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ItemRequestMapperTest {

    public static final long ID = 1L;
    public static final LocalDateTime CREATED_DATE = LocalDateTime.now();

    private ItemRequest request;
    private ItemRequestIncomingDto incomingDto;
    private ItemRepository itemRepository;

    @BeforeEach
    public void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        request = new ItemRequest(ID, "description", ID, CREATED_DATE);
        incomingDto = new ItemRequestIncomingDto("description");
    }

    @Test
    public void toItemRequestTest() {
        ItemRequest result = ItemRequestMapper.toItemRequest(incomingDto, ID);

        assertNotNull(result);
        assertEquals(ID, result.getRequestorId());
        assertEquals(incomingDto.getDescription(), result.getDescription());
    }

    @Test
    public void toPostResponseDtoTest() {
        ItemRequestResponseDto result = ItemRequestMapper.toItemRequestResponseDto(request);

        assertNotNull(result);
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getCreated(), result.getCreated());
    }

    @Test
    public void toRequestWithItemsDtoTest() {
        ItemRequestInItemDto result = ItemRequestMapper.toItemRequestWithItemsDto(request, new ArrayList<>());

        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void toRequestWithItemsDtoListTest() {
        List<ItemRequest> requests = Collections.singletonList(request);
        Page<ItemRequest> page = new PageImpl<>(requests);

        List<ItemRequestInItemDto> fromList = ItemRequestMapper.toItemRequestWithItemsDtoList(page, itemRepository);
        List<ItemRequestInItemDto> fromPage = ItemRequestMapper.toItemRequestWithItemsDtoList(requests, itemRepository);

        assertNotNull(fromList);
        assertNotNull(fromPage);
        assertEquals(request.getId(), fromList.get(0).getId());
        assertEquals(request.getId(), fromPage.get(0).getId());
        assertTrue(fromList.get(0).getItems().isEmpty());
        assertTrue(fromPage.get(0).getItems().isEmpty());
    }
}
