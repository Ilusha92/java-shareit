package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.exceptions.BookerAndOwnerIdException;
import ru.practicum.shareit.booking.exceptions.UnavailableItemException;
import ru.practicum.shareit.booking.exceptions.WrongStateException;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookingServiceTest {

    public static final long ID = 1L;
    public static final int FROM_VALUE = 0;
    public static final int SIZE_VALUE = 20;
    public static final LocalDateTime DATE = LocalDateTime.now();

    private BookingService bookingService;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;

    private User user;
    private Item item;
    private User owner;
    private Booking booking;
    private BookingIncomingDto bookingIncomingDto;

    @BeforeEach
    public void beforeEach() {
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(userRepository, itemRepository, bookingRepository);

        bookingIncomingDto = new BookingIncomingDto(ID, ID, DATE, DATE.plusDays(7));
        user = new User(ID, "name", "user@emali.com");
        owner = new User(ID + 1, "owner", "user2@email.ru");
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
    public void createBookingTest() {

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        BookingResponseDto result = bookingService.createBooking(bookingIncomingDto, ID);

        assertNotNull(result);
        assertEquals(bookingIncomingDto.getItemId(), result.getItem().getId());
        assertEquals(bookingIncomingDto.getStart(), result.getStart());
        assertEquals(bookingIncomingDto.getEnd(), result.getEnd());
    }

    @Test
    public void createBookingIllegalArgumentTest() {
        LocalDateTime date = LocalDateTime.now();
        bookingIncomingDto.setStart(date);
        bookingIncomingDto.setEnd(date.minusDays(1));
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> {
                    bookingService.createBooking(bookingIncomingDto, ID);
                });
        assertNotNull(e);
    }

    @Test
    public void createUnavailableBooking() {
        item.setAvailable(false);

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        UnavailableItemException e = assertThrows(UnavailableItemException.class,
                () -> {
                    bookingService.createBooking(bookingIncomingDto, ID);
                });
        assertNotNull(e);
    }

    @Test
    public void createInvalidBookingTest() {
        item.setOwnerId(user.getId());
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        BookerAndOwnerIdException e = assertThrows(BookerAndOwnerIdException.class,
                () -> {
                    bookingService.createBooking(bookingIncomingDto, ID);
                });
        assertNotNull(e);
    }

    @Test
    public void patchBookingTest() {
        booking.setStatus(Status.WAITING);

        when(bookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(booking));

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        BookingResponseDto result = bookingService.patchBooking(ID, true, ID + 1);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    public void patchBookingNoSuchElementExceptionTest() {
        booking.setStatus(Status.WAITING);
        item.setOwnerId(ID);
        when(bookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(booking));

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        Exception e = assertThrows(NoSuchElementException.class,
                () -> {
                    bookingService.patchBooking(ID, true, ID + 1);
                });
        assertNotNull(e);
    }

    @Test
    public void patchBookingIllegalArgumentExceptionTest() {
        booking.setStatus(Status.WAITING);
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(booking));

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> {
                    bookingService.patchBooking(ID, true, ID + 1);
                });
        assertNotNull(e);
    }

    @Test
    public void findByIdTest() {
        item.setOwnerId(owner.getId());

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(booking));

        BookingResponseDto result = bookingService.findById(ID, ID);

        assertNotNull(result);
        assertEquals(ID, result.getId());
    }

    @Test
    public void findByIdNoSuchElementExceptionTest() {
        user.setId(ID + 10);
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(booking));

        Exception e = assertThrows(NoSuchElementException.class,
                () -> {
                    bookingService.findById(ID, ID);
                });
        assertNotNull(e);
    }

    @Test
    public void findAllByBookerStateRejectedTest() {

        Booking bookingRejected = new Booking(ID,
                DATE,
                DATE.plusDays(7),
                item,
                user,
                Status.REJECTED);
        bookingRepository.save(bookingRejected);

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdAndStatus(any(Long.class), any(Status.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByBooker("REJECTED", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStateWaitingTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdAndStatus(any(Long.class), any(Status.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByBooker("WAITING", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStateCurrentTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdCurrent(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByBooker("CURRENT", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStateFutureTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdAndStartIsAfter(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByBooker("FUTURE", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStatePastTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerIdAndEndIsBefore(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByBooker("PAST", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerStateAllTest() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerId(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> result = bookingService
                .findAllByBooker("ALL", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByBookerUnsupportedStatus() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findByBookerId(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        WrongStateException e = assertThrows(WrongStateException.class,
                () -> {
                    bookingService
                            .findAllByBooker("UNSUPPORTED", ID, FROM_VALUE, SIZE_VALUE);
                });
        assertNotNull(e);
    }

    @Test
    public void findAllByItemOwnerStateRejectedTest() {

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerIdAndStatus(any(Long.class), any(Status.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByItemOwner("REJECTED", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateWaitingTest() {

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerIdAndStatus(any(Long.class), any(Status.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByItemOwner("WAITING", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateCurrentTest() {

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingsByItemOwnerCurrent(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByItemOwner("CURRENT", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateFutureTest() {

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerIdAndStartIsAfter(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByItemOwner("FUTURE", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStatePastTest() {

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerIdAndEndIsBefore(any(Long.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByItemOwner("PAST", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findAllByItemOwnerStateAllTest() {

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByItemOwnerId(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));

        List<BookingDto> result = bookingService
                .findAllByItemOwner("ALL", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}