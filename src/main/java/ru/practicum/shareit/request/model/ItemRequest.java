package ru.practicum.shareit.request.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
public class ItemRequest {

    private long requestId;
    private String description;
    private long requesterId;
    private LocalDateTime createDT;
}
