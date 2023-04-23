package ru.practicum.shareit.error_handler;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.exceptions.BookerAndOwnerIdException;
import ru.practicum.shareit.booking.exceptions.UnavailableItemException;
import ru.practicum.shareit.booking.exceptions.WrongStateException;
import ru.practicum.shareit.error.ErrorHandler;
import ru.practicum.shareit.error.ErrorResponse;
import ru.practicum.shareit.item.exceptions.CommentException;
import ru.practicum.shareit.item.exceptions.DeniedAccessException;
import ru.practicum.shareit.item.exceptions.OwnerNotFoundException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorHandlerTest {

    private final ErrorHandler handler = new ErrorHandler();

    @Test
    public void handleOwnerNotFoundExceptionTest() {
        OwnerNotFoundException e = new OwnerNotFoundException("Не найден владелец вещи ");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), e.getMessage());
    }

    @Test
    public void handleDeniedAccessExceptionTest() {
        DeniedAccessException e = new DeniedAccessException("Отказано в доступе ");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), e.getMessage());
    }

    @Test
    public void handleNoSuchElementExceptionTest() {
        NoSuchElementException e = new NoSuchElementException("Ошибка поиска элемента 404: ");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getError(), "Ошибка поиска элемента 404: ");
        assertEquals(errorResponse.getDescription(), "Ошибка поиска элемента 404: ");
    }

    @Test
    public void handleUnavailableBookingExceptionTest() {
        BookerAndOwnerIdException e = new BookerAndOwnerIdException("Ошибка бронирования 400: ");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), "Ошибка бронирования 400: ");
    }

    @Test
    public void handleIllegalArgumentExceptionTest() {
        IllegalArgumentException e = new IllegalArgumentException("Передано недопустимое значение 400: ");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), "Передано недопустимое значение 400: ");
    }

    @Test
    public void handleUnsupportedStatusExceptionTest() {
        WrongStateException e = new WrongStateException("Unknown state: UNSUPPORTED_STATUS");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), "Unknown state: UNSUPPORTED_STATUS");
    }

    @Test
    public void handleInvalidBookingExceptionTest() {
        UnavailableItemException e = new UnavailableItemException("недопустимое бронирование 404: ");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), "недопустимое бронирование 404: ");
    }

    @Test
    public void handleCommentExceptionTest() {
        CommentException e = new CommentException("невозможно оставить комментарий 400: ");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), "невозможно оставить комментарий 400: ");
    }
}
