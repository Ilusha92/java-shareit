package ru.practicum.shareit.requests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User requestor;
    private ItemRequest request;

    @BeforeEach
    public void beforeEach() {
        LocalDateTime dateTime = LocalDateTime.now();
        user = userRepository.save(new User(null, "user", "user@email.com"));
        requestor = userRepository.save(new User(null, "requestor", "requestor@email.com"));
        request = itemRequestRepository.save(new ItemRequest(null, "request", requestor.getId(), dateTime));
    }

    @Test
    public void findRequestByRequestorOrderByCreatedDescTest() {
        List<ItemRequest> result = itemRequestRepository
                .findRequestByRequestorIdOrderByCreatedDesc(requestor.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(request.getDescription(), result.get(0).getDescription());
        assertEquals(request.getRequestorId(), result.get(0).getRequestorId());
        assertEquals(request.getCreated(), result.get(0).getCreated());
    }

    @Test
    public void findAllTest() {
        Page<ItemRequest> result = itemRequestRepository.findAll(user.getId(), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(request.getDescription(), result.getContent().get(0).getDescription());
        assertEquals(request.getRequestorId(), result.getContent().get(0).getRequestorId());
        assertEquals(request.getCreated(), result.getContent().get(0).getCreated());
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }
}
