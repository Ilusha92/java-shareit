package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class User {

    private long id;
    private String name;
    private String email;

}
