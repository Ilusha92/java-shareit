package ru.practicum.shareit.request.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
public class ItemRequestDto {

    @NotNull
    private String description;
    private Long requesterId;
    private LocalDateTime createDT;

}
