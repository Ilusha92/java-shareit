package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.model.ItemRequestInItemDto;
import ru.practicum.shareit.request.model.ItemRequestIncomingDto;
import ru.practicum.shareit.request.model.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {

    public static final String FROM_VALUE = "0";
    public static final String SIZE_VALUE = "20";
    public static final Long USER_ID_VALUE = 1L;
    public static final String FROM_PARAM = "from";
    public static final String SIZE_PARAM = "size";
    public static final String TEST_DESCRIPTION = "description";
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @MockBean
    ItemRequestService itemRequestService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Test
    public void createRequestTest() throws Exception {
        ItemRequestIncomingDto requestDto = createPostRequestDto(TEST_DESCRIPTION);
        long requestId = 1;
        LocalDateTime creationDate = LocalDateTime.now();
        ItemRequestResponseDto responseDto = createPostResponseDto(requestId, requestDto, creationDate);

        when(itemRequestService.createRequest(any(ItemRequestIncomingDto.class), any(Long.class)))
                .thenReturn(responseDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(responseDto.getDescription()), String.class));

        verify(itemRequestService, times(1))
                .createRequest(any(ItemRequestIncomingDto.class), any(Long.class));
    }

    @Test
    public void findAllByUserIdTest() throws Exception {
        when(itemRequestService.findAllByUserId(any(Long.class)))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/requests")
                        .header(USER_ID_HEADER, USER_ID_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1)).findAllByUserId(any(Long.class));
    }

    @Test
    public void findAllTest() throws Exception {
        when(itemRequestService.findAll(any(Integer.class), any(Integer.class), any(Long.class)))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/requests/all")
                        .param(FROM_PARAM, FROM_VALUE)
                        .param(SIZE_PARAM, SIZE_VALUE)
                        .header(USER_ID_HEADER, USER_ID_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1))
                .findAll(any(Integer.class), any(Integer.class), any(Long.class));
    }

    @Test
    public void findByIdTest() throws Exception {
        ItemRequestInItemDto dto = new ItemRequestInItemDto();
        dto.setId(1L);
        dto.setDescription("description");
        dto.setItems(Collections.emptyList());

        when(itemRequestService.findById(any(Long.class), any(Long.class)))
                .thenReturn(dto);

        mvc.perform(get("/requests/1")
                        .header(USER_ID_HEADER, USER_ID_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dto.getDescription()), String.class))
                .andExpect(jsonPath("$.items", is(dto.getItems()), List.class));

        verify(itemRequestService, times(1)).findById(any(Long.class), any(Long.class));
    }

    private ItemRequestResponseDto createPostResponseDto(Long id, ItemRequestIncomingDto dto, LocalDateTime date) {
        ItemRequestResponseDto responseDto = new ItemRequestResponseDto();
        responseDto.setDescription(dto.getDescription());
        responseDto.setId(1L);
        responseDto.setId(id);
        responseDto.setCreated(date);
        return responseDto;
    }

    private ItemRequestIncomingDto createPostRequestDto(String description) {
        return new ItemRequestIncomingDto(description);
    }
}
