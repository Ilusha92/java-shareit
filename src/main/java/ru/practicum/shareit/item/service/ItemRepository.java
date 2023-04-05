package ru.practicum.shareit.item.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(" select i from items i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')) and i.available = true")
    List<Item> search(String text);

    @Query("SELECT b FROM  bookings b " +
            "WHERE b.item.id = :itemId AND b.item.ownerId = :userId AND b.status = 'APPROVED' AND b.end < :now " +
            "ORDER BY b.start DESC")
    Booking findLastBooking(LocalDateTime now, Long userId, Long itemId);
}
