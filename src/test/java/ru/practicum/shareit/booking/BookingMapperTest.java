package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingMapperTest {

    public static final long ID = 1L;
    public static final LocalDateTime DATE = LocalDateTime.now();

    private User user;
    private Item item;
    private Booking booking;
    private BookingIncomingDto bookingincomingDto;

    @BeforeEach
    public void beforeEach() {
        bookingincomingDto = new BookingIncomingDto(ID, ID, DATE, DATE.plusDays(7));
        user = new User(ID, "name", "user@emali.com");
        item = new Item(
                ID,
                "name",
                "description",
                true,
                ID + 1,
                ID + 1);
        booking = new Booking(ID,
                DATE,
                DATE.plusDays(7),
                item,
                user,
                Status.APPROVED);
    }

    @Test
    public void toNewBookingTest() {
        Booking result = BookingMapper.toNewBooking(bookingincomingDto, item, user);

        assertNotNull(result);
        assertEquals(bookingincomingDto.getStart(), result.getStart());
        assertEquals(bookingincomingDto.getEnd(), result.getEnd());
        assertEquals(item.getId(), result.getItem().getId());
        assertEquals(user.getId(), result.getBooker().getId());
    }

    @Test
    public void toBookingResponseDtoTest() {
        BookingResponseDto result = BookingMapper.toBookingResponseDto(booking, user, item);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
    }

    @Test
    public void toBookingDtoTest() {
        BookingDto result = BookingMapper.toBookingDto(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getBooker().getId(), result.getBooker().getId());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
        assertEquals(booking.getItem().getName(), result.getName());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
    }

    @Test
    public void toBookingInItemDtoTest() {
        BookingInItemDto result = BookingMapper.toBookingInItemDto(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getBooker().getId(), result.getBookerId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
    }

    @Test
    public void toListBookingDtoTest() {
        List<BookingDto> result = BookingMapper.toListBookingDto(List.of(booking));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getId(), booking.getId());
    }
}
