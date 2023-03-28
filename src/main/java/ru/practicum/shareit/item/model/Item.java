package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "items")
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "available",nullable = false)
    private Boolean available;
    @Column(name = "ownerId",nullable = false)
    private Long ownerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return id.equals(item.id) && name.equals(item.name) && description.equals(item.description) &&
                available.equals(item.available) && ownerId.equals(item.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, ownerId);
    }
}
