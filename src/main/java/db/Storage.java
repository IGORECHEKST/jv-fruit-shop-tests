package db;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    public static final Map<String, Integer> fruitInventory = new HashMap<>();

    public static void set(String fruit, int quantity) {
        fruitInventory.put(fruit, quantity);
    }

    public static int get(String fruit) {
        return fruitInventory.getOrDefault(fruit, 0);
    }
}
