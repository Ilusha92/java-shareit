package ru.practicum.shareit.request.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestIncomingDto {

    @NotBlank
    String description;
}
