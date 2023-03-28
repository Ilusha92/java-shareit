//package ru.practicum.shareit.item.storage.impl;
//
//import org.springframework.stereotype.Repository;
//import ru.practicum.shareit.item.exceptions.DeniedAccessException;
//import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.storage.dao.ItemDao;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class InMemoryItemStorage implements ItemDao {
//
//    private final Map<Long, Item> itemMap = new HashMap<>();
//    private long idCounter = 1;
//
//    @Override
//    public Item createItem(Item item) {
//        item.setId(idCounter++);
//        itemMap.put(item.getId(), item);
//        return item;
//    }
//
//    @Override
//    public Item updateItem(Item item) {
//        long itemId = item.getId();
//        if (!itemMap.containsKey(itemId)) {
//            throw new ItemNotFoundException("this item not found in storage");
//        }
//        Item itemFromStorage = itemMap.get(itemId);
//        if (!itemFromStorage.getOwnerId().equals(item.getOwnerId())) {
//            throw new DeniedAccessException("userId: " + item.getOwnerId() + "is not owner of this item" + itemId);
//        }
//        refreshItem(itemFromStorage, item);
//        return itemFromStorage;
//    }
//
//    @Override
//    public List<Item> getAllItems(Long userId) {
//        List<Item> result = new ArrayList<>();
//        for (Item item : itemMap.values()) {
//            if (item.getOwnerId().equals(userId))
//                result.add(item);
//        }
//        return result;
//    }
//
//    @Override
//    public Item getItemById(Long id) {
//        return itemMap.get(id);
//    }
//
//    @Override
//    public List<Item> findItemsByRequest(String text) {
//        List<Item> result = new ArrayList<>();
//        String wantedItem = text.toLowerCase();
//
//        for (Item item : itemMap.values()) {
//            String itemName = item.getName().toLowerCase();
//            String itemDescription = item.getDescription().toLowerCase();
//
//            if ((itemName.contains(wantedItem) || itemDescription.contains(wantedItem))
//                    && item.getAvailable().equals(true)) {
//                result.add(item);
//            }
//        }
//        return result;
//    }
//
//    private void refreshItem(Item oldEntry, Item newEntry) {
//        String name = newEntry.getName();
//        if (name != null) {
//            oldEntry.setName(name);
//        }
//
//        String description = newEntry.getDescription();
//        if (description != null) {
//            oldEntry.setDescription(description);
//        }
//
//        Boolean available = newEntry.getAvailable();
//        if (available != null) {
//            oldEntry.setAvailable(available);
//        }
//    }
//}
