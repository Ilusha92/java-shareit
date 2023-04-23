package ru.practicum.shareit.booking.exceptions;

public class WrongStateException extends RuntimeException {
    public WrongStateException(String message) {
        super(message);
    }
}
