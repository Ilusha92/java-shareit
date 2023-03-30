package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingDto dto, Long userId);

//    Booking patchBooking(Long bookingId, Boolean approved, Long userId);
//
//    Booking findById(Long bookingId, Long userId);
//
//    List<Booking> findAllByBooker(String state, Long userId);
//
//    List<Booking> findAllByItemOwner(String state, Long userId);


}
