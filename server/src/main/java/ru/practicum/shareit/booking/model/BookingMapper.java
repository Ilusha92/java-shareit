package ru.practicum.shareit.booking.model;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public static Booking toNewBooking(BookingIncomingDto bookingIncomingDto, Item item, User booker) {
        return new Booking(null, bookingIncomingDto.getStart(), bookingIncomingDto.getEnd(), item, booker, Status.WAITING);
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking, User booker, Item item) {
        return new BookingResponseDto(booking.getId(), booking.getStatus(), booker, booking.getStart(), booking.getEnd(), item, item.getName());
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),booking.getStart(), booking.getEnd(), booking.getStatus(),
                booking.getBooker(), booking.getItem(), booking.getItem().getName());
    }

    public static List<BookingDto> toListBookingDto(List<Booking> bookings) {
            return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    public static BookingInItemDto toBookingInItemDto(Booking booking) {
        if (booking == null) return null;

        BookingInItemDto dto = new BookingInItemDto();
        dto.setId(booking.getId());
        dto.setBookerId(booking.getBooker().getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }

}
