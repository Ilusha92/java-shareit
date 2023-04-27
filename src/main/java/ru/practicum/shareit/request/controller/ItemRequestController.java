package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestInItemDto;
import ru.practicum.shareit.request.model.ItemRequestIncomingDto;
import ru.practicum.shareit.request.model.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto createRequest(@Valid
                                                @RequestBody ItemRequestIncomingDto dto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.createRequest(dto, userId);
    }

    @GetMapping
    public List<ItemRequestInItemDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.findAllByUserId(userId);
    }

    @GetMapping ("/all")
    public List<ItemRequestInItemDto> findAll(@RequestParam(defaultValue = "0")
                                             @Min(0) int from,
                                             @RequestParam(defaultValue = "20")
                                             @Min(0) int size,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.findAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestInItemDto findById(@PathVariable Long requestId,
                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.findById(requestId, userId);
    }
}
