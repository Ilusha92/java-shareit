package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    BookingService bookingService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        Item item = itemMapper.toItem(itemDto, ownerId);
        return itemMapper.toItemDto(itemService.createItem(item));
    }
}
