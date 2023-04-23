package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.ItemInRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestInItemDto {

    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemInRequestDto> items;
}
