package ru.practicum.shareit.booking.exceptions;

public class TimeValidationException extends IllegalArgumentException {
    public TimeValidationException(String message) {
        super(message);
    }
}
