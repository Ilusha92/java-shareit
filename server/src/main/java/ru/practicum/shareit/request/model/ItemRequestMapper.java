package ru.practicum.shareit.request.model;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemInRequestDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestIncomingDto dto, Long requestorId) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setRequestorId(requestorId);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest request) {
        ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static ItemRequestInItemDto toItemRequestWithItemsDto(ItemRequest request, List<Item> items) {
        List<ItemInRequestDto> itemsDto = ItemMapper.toRequestItemDtoList(items);
        ItemRequestInItemDto dto = new ItemRequestInItemDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setItems(itemsDto);
        return dto;
    }

    public static List<ItemRequestInItemDto> toItemRequestWithItemsDtoList(Page<ItemRequest> requests,
                                                                      ItemRepository itemRepository) {
        return requests.stream()
                .map((ItemRequest request) -> {
                    List<Item> items = itemRepository.findAllByRequestId(request.getId());
                    return ItemRequestMapper.toItemRequestWithItemsDto(request, items);
                }).collect(Collectors.toList());
    }

    public static List<ItemRequestInItemDto> toItemRequestWithItemsDtoList(List<ItemRequest> requests,
                                                                      ItemRepository repository) {
        List<ItemRequestInItemDto> result = new ArrayList<>();
        if (requests != null && !requests.isEmpty()) {
            for (ItemRequest itemRequest : requests) {
                List<Item> items = repository.findAllByRequestId(itemRequest.getId());
                ItemRequestInItemDto requestDto = ItemRequestMapper.toItemRequestWithItemsDto(itemRequest, items);
                result.add(requestDto);
            }
        }
        return result;
    }


}
