package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exceptions.BookerAndOwnerIdException;
import ru.practicum.shareit.booking.exceptions.UnavailableItemException;
import ru.practicum.shareit.booking.exceptions.WrongStateException;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static ru.practicum.shareit.booking.model.Status.REJECTED;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingIncomingDto bookingIncomingDto, Long userId) {

        if (!bookingIncomingDto.getEnd().isAfter(bookingIncomingDto.getStart())) {
            throw new IllegalArgumentException("Validation exception with startBookingTime and endBookingTime, check the values");
        }

        User user = userRepository.findById(userId).orElseThrow();
        Item item = itemRepository.findById(bookingIncomingDto.getItemId()).orElseThrow();

        if (userId.equals(item.getOwnerId())) {
            throw new BookerAndOwnerIdException("Owner cant book his item");
        }
        if (!item.getAvailable()) {
            throw new UnavailableItemException("Item is not available now");
        }

        Booking booking = BookingMapper.toNewBooking(bookingIncomingDto, item, user);
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingResponseDto(booking, user, item);
    }

    @Override
    @Transactional
    public BookingResponseDto patchBooking(Long bookingId, Boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow();

        if (!item.getOwnerId().equals(userId)) {
            throw new NoSuchElementException("user is not owner, only owner can patch booking");
        }
        Status status = approved ? Status.APPROVED : REJECTED;

        if (booking.getStatus().equals(status)) {
            throw new IllegalArgumentException();
        }

        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingResponseDto(booking, booking.getBooker(), item);
    }

    @Override
    public BookingResponseDto findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Booking ID %s doesn't exist.", bookingId)));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwnerId().equals(userId)) {
            throw new NoSuchElementException(String.format("User ID %s didn't create booking ID %s.", userId, bookingId));
        }
        return BookingMapper.toBookingResponseDto(booking, booking.getBooker(), booking.getItem());
    }

    @Override
    public List<BookingDto> findAllByBooker(String state, Long userId, int from, int size) {
        State status;
        try {
            status = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new WrongStateException("illegal state");
        }
        userRepository.findById(userId).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        Sort sort = Sort.by("start").descending();

        Pageable pageable = PageRequest.of(from / size, size, sort);

        switch (status) {
            case REJECTED :
                bookings = bookingRepository
                        .findByBookerIdAndStatus(userId, REJECTED, pageable).toList();
                break;
            case WAITING :
                bookings = bookingRepository
                        .findByBookerIdAndStatus(userId, WAITING, pageable).toList();
                break;
            case CURRENT :
                bookings = bookingRepository
                        .findByBookerIdCurrent(userId, now, pageable).toList();
                break;
            case FUTURE :
                bookings = bookingRepository
                        .findByBookerIdAndStartIsAfter(userId, now, pageable).toList();
                break;
            case PAST :
                bookings = bookingRepository
                        .findByBookerIdAndEndIsBefore(userId, now, pageable).toList();
                break;
            case ALL :
                bookings = bookingRepository.findByBookerId(userId, pageable).toList();
                break;
            default :
                throw new IllegalArgumentException("illegal state");
        }
        return BookingMapper.toListBookingDto(bookings);
    }

    @Override
    public List<BookingDto> findAllByItemOwner(String state, Long userId, int from, int size) {
        State value;
        try {
            value = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new WrongStateException("illegal state");
        }
        userRepository.findById(userId).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        Sort sort = Sort.by("start").descending();

        Pageable pageable = PageRequest.of(from / size, size, sort);

        switch (value) {
            case REJECTED :
                bookings = bookingRepository
                        .findBookingByItemOwnerIdAndStatus(userId, REJECTED, pageable).toList();
                break;
            case WAITING :
                bookings = bookingRepository
                        .findBookingByItemOwnerIdAndStatus(userId, WAITING, pageable).toList();
                break;
            case CURRENT :
                bookings = bookingRepository.findBookingsByItemOwnerCurrent(userId, now, pageable).toList();
                break;
            case FUTURE :
                bookings = bookingRepository
                        .findBookingByItemOwnerIdAndStartIsAfter(userId, now, pageable).toList();
                break;
            case PAST :
                bookings = bookingRepository
                        .findBookingByItemOwnerIdAndEndIsBefore(userId, now, pageable).toList();
                break;
            case ALL :
                bookings = bookingRepository
                        .findBookingByItemOwnerId(userId, pageable).toList();
                break;
            default :
                throw new IllegalArgumentException("illegal state");
        }
        return BookingMapper.toListBookingDto(bookings);
    }

}
