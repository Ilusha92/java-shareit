package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndEndIsBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItemOwnerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItemOwnerId(Long bookerId, Sort sort);

    List<Booking> findBookingByItemIdAndEndBefore(Long itemId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItemIdAndStartAfter(Long itemId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItemIdAndEndBeforeAndStatus(Long itemId, LocalDateTime now, Sort sort, Status status);

    List<Booking> findBookingByItemIdAndStartAfterAndStatus(Long itemId, LocalDateTime now, Sort sort, Status status);

    @Query("select b from bookings b " +
            "where b.booker.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc ")
    List<Booking> findByBookerIdCurrent(Long userId,LocalDateTime now);

    @Query("select b from bookings b " +
            "where b.item.ownerId = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start asc")
    List<Booking> findBookingsByItemOwnerCurrent(Long userId, LocalDateTime now);

    @Query("select b from bookings b " +
            " where b.item.id = ?1 " +
            " and b.booker.id = ?2" +
            " and b.end < ?3")
    List<Booking> findBookingsForAddComments(Long itemId, Long userId, LocalDateTime now);
}
