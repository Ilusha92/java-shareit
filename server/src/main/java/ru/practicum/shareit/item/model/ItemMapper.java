package ru.practicum.shareit.item.model;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;

import java.util.List;
import java.util.stream.Collectors;

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
        dto.setRequestId(item.getRequestId());
        return dto;
    }

    public static Item toItem(ItemDto itemDto, Long ownerId) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwnerId(ownerId);
        item.setRequestId(itemDto.getRequestId());
        return item;
    }

    public static ItemDto toItemDtoForOwner(Item item,
                                            Booking lastBooking,
                                            Booking nextBooking,
                                            List<Comment> comments) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setLastBooking(BookingMapper.toBookingInItemDto(lastBooking));
        dto.setNextBooking(BookingMapper.toBookingInItemDto(nextBooking));
        if (comments != null) {
            dto.setComments(CommentMapper.toCommentDtoList(comments));
        }
        return dto;
    }

    public static ItemInRequestDto toRequestItemDto(Item item) {
        ItemInRequestDto dto = new ItemInRequestDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequestId());
        dto.setOwnerId(item.getOwnerId());
        return dto;
    }

    public static List<ItemInRequestDto> toRequestItemDtoList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toRequestItemDto)
                .collect(Collectors.toList());
    }


}
