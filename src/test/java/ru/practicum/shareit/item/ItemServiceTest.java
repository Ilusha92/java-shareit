package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exceptions.CommentException;
import ru.practicum.shareit.item.exceptions.DeniedAccessException;
import ru.practicum.shareit.item.exceptions.OwnerNotFoundException;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceTest {

    public static final long ID = 1L;
    public static final int FROM_VALUE = 0;
    public static final int SIZE_VALUE = 20;
    public static final LocalDateTime CREATED_DATE = LocalDateTime.now();

    private ItemService itemService;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;

    private Item item;
    private User user;
    private ItemDto itemDto;
    private Comment comment;
    private Booking booking;
    private CreateCommentDto createCommentDto;

    @BeforeEach
    public void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository);

        item = new Item(
                ID,
                "name",
                "description",
                true,
                ID,
                ID + 1);
        itemDto = new ItemDto(
                ID,
                "name",
                "description",
                true,
                null,
                null,
                null,
                ID + 1);

        user = new User(ID, "name", "user@emali.com");
        comment = new Comment(ID, "comment", item, user, CREATED_DATE);
        createCommentDto = new CreateCommentDto("comment");
        booking = new Booking(ID,
                CREATED_DATE,
                CREATED_DATE.plusDays(7),
                item,
                user,
                Status.APPROVED);
    }

    @Test
    public void createItemTest() {
        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(user));

        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        ItemDto result = itemService.createItem(itemDto, ID);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        assertEquals(itemDto.getRequestId(), result.getRequestId());
    }

    @Test
    public void createItemOwnerNotFoundTest() {
        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        OwnerNotFoundException e = assertThrows(OwnerNotFoundException.class,
                () -> {
                    itemService.createItem(itemDto, ID);
                });
        assertNotNull(e);
    }

    @Test
    public void createCommentTest() {
        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingsForAddComments(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);

        CommentDto result = itemService.createComment(createCommentDto, ID, ID);

        assertNotNull(result);
        assertEquals(createCommentDto.getText(), result.getText());
        assertEquals(user.getName(), result.getAuthorName());
    }

    @Test
    public void createCommentExceptionTest() {
        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingsForAddComments(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        CommentException result = assertThrows(CommentException.class, () -> {
            itemService.createComment(createCommentDto, ID, ID);
        });

        assertNotNull(result);
    }

    @Test
    public void updateItemTest() {
        itemDto.setName("updatedName");
        item.setName("updatedName");

        when(commentRepository.findByItemId(any(Long.class)))
                .thenReturn(new ArrayList<>());

        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        ItemDto result = itemService.updateItem(itemDto, itemDto.getId(), user.getId());

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
    }

    @Test
    public void updateItemDeniedExcessTest() {
        item.setOwnerId(ID + 1);

        when(commentRepository.findByItemId(any(Long.class)))
                .thenReturn(new ArrayList<>());

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));


        DeniedAccessException exception = assertThrows(DeniedAccessException.class, () -> {
            itemService.updateItem(itemDto, itemDto.getId(), user.getId());
        });

        assertNotNull(exception);
    }

    @Test
    public void getItemByIdTest() {
        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        when(commentRepository.findByItemId(any(Long.class)))
                .thenReturn(new ArrayList<>());

        ItemDto result = itemService.getItemById(ID, ID);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    public void getAllItemsTest() {
        when(itemRepository.findAll(any(Long.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        List<ItemDto> result = itemService.getAllItems(ID, FROM_VALUE, SIZE_VALUE);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findItemsByRequestTest() {
        when(itemRepository.search(any(String.class), any(Pageable.class)))
                .thenReturn(new ArrayList<>());

        List<ItemDto> result = itemService.findItemsByRequest("request", ID, FROM_VALUE, SIZE_VALUE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findItemsByBlankRequest() {
        List<ItemDto> result = itemService.findItemsByRequest("", ID, FROM_VALUE, SIZE_VALUE);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

//    @Test
//    public void findItemsByRequestSeveralRequestsTest() {
//        Item item2 = new Item(
//                null,
//                "item2",
//                "description",
//                true,
//                ID + 1,
//                null);
//        List<Item> items = List.of(item, item2);
//        when(itemRepository.search(any(String.class), any(Pageable.class)))
//                .thenReturn(new ArrayList<>());
//
//        List<ItemDto> result = itemService.findItemsByRequest("description", ID, FROM_VALUE, SIZE_VALUE);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }
}
