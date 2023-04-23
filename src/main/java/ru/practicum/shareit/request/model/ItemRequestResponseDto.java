package ru.practicum.shareit.request.model;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestResponseDto {

    private Long id;
    private String description;
    private LocalDateTime created;
}
