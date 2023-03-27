package ru.practicum.shareit.item.exceptions;

public class DeniedAccessException extends RuntimeException {
    public DeniedAccessException(String message) {
        super(message);
    }
}