package ru.practicum.shareit.item.exceptions;

public class OwnerNotFoundException extends RuntimeException {
    public OwnerNotFoundException(String message) {
        super(message);
    }
}
