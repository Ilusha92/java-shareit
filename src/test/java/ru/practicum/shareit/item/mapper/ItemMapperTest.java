package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemMapperTest {

    public static final long ID = 1L;
    public static final LocalDateTime CREATED_DATE = LocalDateTime.now();

    private Item item;
    private ItemDto itemDto;
    private Comment comment;

    @BeforeEach
    public void beforeEach() {
        item = new Item(
                ID,
                "name",
                "description",
                true,
                ID,
                ID + 1);

        itemDto = new ItemDto(
                ID,
                "name",
                "description",
                true,
                null,
                null,
                null,
                ID + 1);

        User user = new User(ID, "name", "user@emali.com");
        comment = new Comment(ID, "comment", item, user, CREATED_DATE);

        Booking booking = new Booking(ID,
                CREATED_DATE,
                CREATED_DATE.plusDays(7),
                item,
                user,
                Status.APPROVED);
    }

    @Test
    public void toItemDtoForOwnerTest() {
        ItemDto resultWithoutBookings = ItemMapper
                .toItemDtoForOwner(item, null, null, Collections.singletonList(comment));
        ItemDto resultWithBookings = ItemMapper
                .toItemDtoForOwner(item, null, null, Collections.singletonList(comment));

        assertNotNull(resultWithoutBookings);
        assertNotNull(resultWithBookings);
        assertEquals(item.getId(), resultWithBookings.getId());
        assertEquals(item.getId(), resultWithoutBookings.getId());
        assertFalse(resultWithBookings.getComments().isEmpty());
        assertFalse(resultWithBookings.getComments().isEmpty());
    }

    @Test
    public void toItemTest() {
        Item result = ItemMapper.toItem(itemDto, ID);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(ID, result.getOwnerId());
    }

    @Test
    public void toRequestItemDtoTest() {
        ItemInRequestDto result = ItemMapper.toRequestItemDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
    }

    @Test
    public void toRequestItemDtoListTest() {
        List<ItemInRequestDto> result = ItemMapper
                .toRequestItemDtoList(Collections.singletonList(item));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(item.getId(), result.get(0).getId());
        assertEquals(item.getName(), result.get(0).getName());
        assertEquals(item.getDescription(), result.get(0).getDescription());
        assertEquals(item.getAvailable(), result.get(0).getAvailable());
        assertEquals(item.getRequestId(), result.get(0).getRequestId());
        assertEquals(item.getOwnerId(), result.get(0).getOwnerId());

    }
}
