package ru.practicum.shareit.booking.exceptions;

public class UnavailableItemException extends RuntimeException {

    public UnavailableItemException(String message) {
        super(message);
    }
}
