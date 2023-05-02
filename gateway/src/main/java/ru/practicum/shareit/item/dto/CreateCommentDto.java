package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    @NotBlank
    private String text;
}
