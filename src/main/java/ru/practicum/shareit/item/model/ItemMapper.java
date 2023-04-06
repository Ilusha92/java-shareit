package ru.practicum.shareit.item.model;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;

import java.util.List;

@UtilityClass
public class ItemMapper {

    public static ItemDto toItemDto(Item item, List<Comment> comments) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        if (comments != null) {
            dto.setComments(CommentMapper.toCommentDtoList(comments));
        }
        return dto;
    }

    public static Item toItem(ItemDto itemDto, Long ownerId) {
            return new Item(null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId);
    }

    public static ItemDto toDto(Item item,
                                Booking lastBooking,
                                Booking nextBooking,
                                List<Comment> comments) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setLastBooking(BookingMapper.bookingInItemDto(lastBooking));
        dto.setNextBooking(BookingMapper.bookingInItemDto(nextBooking));
        if (comments != null) {
            dto.setComments(CommentMapper.toCommentDtoList(comments));
        }
        return dto;
    }

}
