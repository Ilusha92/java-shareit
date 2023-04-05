package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.item.exceptions.CommentException;
import ru.practicum.shareit.item.exceptions.DeniedAccessException;
import ru.practicum.shareit.item.exceptions.OwnerNotFoundException;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toItem(itemDto, userId);
        boolean ownerExists = isOwnerExists(item.getOwnerId());
        if (!ownerExists) {
            throw new OwnerNotFoundException("owner not found");
        }
        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item, null);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long ownerId, Long itemId) {
        Item item = ItemMapper.toItem(itemDto, ownerId);
        item.setId(itemId);
        List<Comment> comments = commentRepository.findByItemId(itemId);
        item = itemRepository.save(checkAttributes(item));
        return ItemMapper.toItemDto(item, comments);
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        List<Item> userItems = itemRepository.findAll()
                .stream().filter(item -> item.getOwnerId().equals(userId))
                .collect(Collectors.toList());

        List<ItemDto> result = new ArrayList<>();
        fillItemDtoList(result, userItems, userId);

        result.sort((o1, o2) -> {
            if (o1.getNextBooking() == null && o2.getNextBooking() == null) {
                return 0;
            }
            if (o1.getNextBooking() != null && o2.getNextBooking() == null) {
                return -1;
            }
            if (o1.getNextBooking() == null && o2.getNextBooking() != null) {
                return 1;
            }
            if (o1.getNextBooking().getStart().isBefore(o2.getNextBooking().getStart())) {
                return -1;
            }
            if (o1.getNextBooking().getStart().isAfter(o2.getNextBooking().getStart())) {
                return 1;
            }
            return 0;
        });
        return result;
    }



    @Override
    public List<ItemDto> findItemsByRequest(String text, Long userId) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        List<ItemDto> result = new ArrayList<>();
        List<Item> foundItems = itemRepository.search(text);
        fillItemDtoList(result, foundItems, userId);
        return result;
    }

    @Override
    public CommentDto createComment(CreateCommentDto dto, Long itemId, Long userId) {
        if (dto.getText().isBlank()) throw new CommentException("Comment cant be blank");
        Item item = itemRepository.findById(itemId).orElseThrow();
        User author = userRepository.findById(userId).orElseThrow();

        if (bookingRepository.findBookingsForAddComments(itemId, userId, LocalDateTime.now()).isEmpty()) {
            throw new CommentException("item wasn't in your rent or rent time of this item not overed");
        }
        Comment comment = CommentMapper.toComment(dto, item, author);
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    private boolean isOwnerExists(long ownerId) {
        List<User> users = userRepository.findAll();
        List<User> result = users.stream().filter(user -> user.getId() == ownerId).collect(Collectors.toList());
        return result.size() > 0;
    }

    private Item checkAttributes(Item patch) {
        Item item = itemRepository.findById(patch.getId()).orElseThrow();

        if (!item.getOwnerId().equals(patch.getOwnerId())) {
            throw new DeniedAccessException("user not owner of this item");
        }

        String name = patch.getName();
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }

        String description = patch.getDescription();
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }

        Boolean available = patch.getAvailable();
        if (available != null) {
            item.setAvailable(available);
        }
        return item;
    }

    private void fillItemDtoList(List<ItemDto> targetList, List<Item> foundItems, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        Sort sortDesc = Sort.by("start").descending();
        Sort sortAsc = Sort.by("start").ascending();

        for (Item item : foundItems) {
            List<Comment> comments = commentRepository.findByItemId(item.getId());
            if (item.getOwnerId().equals(userId)) {
                ItemDto dto = constructItemDtoForOwner(item, now, sortDesc, sortAsc, comments);
                targetList.add(dto);
            } else {
                targetList.add(ItemMapper.toItemDto(item, comments));
            }
        }
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        List<Comment> comments = commentRepository.findByItemId(itemId);

        if (item.getOwnerId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            Sort sortDesc = Sort.by("start").descending();
            Sort sortAsc = Sort.by("start").ascending();
            return constructItemDtoForOwner(item, now, sortDesc, sortAsc, comments);
        }
        return ItemMapper.toDto(item, null, null, comments);
    }

    private ItemDto constructItemDtoForOwner(Item item, LocalDateTime now, Sort sortDesc, Sort sortAsc, List<Comment> comments) {
        Booking lastBooking =
                bookingRepository.findBookingByItemIdAndStartBeforeAndStatus(item.getId(), now, sortDesc, Status.APPROVED)
                        .stream().findFirst().orElse(null);
        Booking nextBooking =
                bookingRepository.findBookingByItemIdAndStartAfterAndStatus(item.getId(), now, sortAsc, Status.APPROVED)
                        .stream().findFirst().orElse(null);

        return ItemMapper.toDto(item,
                lastBooking,
                nextBooking,
                comments);
    }
}
