package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingIncomingDto;
import ru.practicum.shareit.booking.model.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingIncomingDto bookingIncomingDto, Long ownerId);

    BookingResponseDto patchBooking(Long bookingId, Boolean approved, Long userId);

    BookingResponseDto findById(Long bookingId, Long userId);

    List<BookingDto> findAllByBooker(String state, Long userId, int from, int size);

    List<BookingDto> findAllByItemOwner(String state, Long userId, int from, int size);


}
