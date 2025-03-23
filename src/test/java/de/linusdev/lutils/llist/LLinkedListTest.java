/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.llist;

import de.linusdev.lutils.collections.llist.LLinkedList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LLinkedListTest {

    public static final String[] NUMBERS = new String[] {"1", "2", "3", "4", "5", "6", "7", "8"};

    @Test
    void size() {
        LLinkedList<String> list = new LLinkedList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");

        assertEquals(list.size(), 8);
    }

    @Test
    void isEmptyAndRemove() {
        LLinkedList<String> list = new LLinkedList<>();
        assertTrue(list.isEmpty());

        list.add("1");
        list.remove(0);
        assertTrue(list.isEmpty());
    }

    @Test
    void contains() {
        LLinkedList<Object> list = new LLinkedList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");

        Object o = new Object();

        assertFalse(list.contains(o));

        list.add(o);
        assertTrue(list.contains(o));

    }

    @Test
    void iterator() {
        LLinkedList<String> list = new LLinkedList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");

        ArrayList<String> arrayList = new ArrayList<>();

        for (String s : list) {
            arrayList.add(s);
        }

        assertIterableEquals(List.of(NUMBERS), arrayList);
    }

    @Test
    void toArray() {
        LLinkedList<String> list = new LLinkedList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");


        assertArrayEquals(NUMBERS, list.toArray(new String[0]));
    }

    @Test
    void add() {
        LLinkedList<String> list = new LLinkedList<>();

        list.add("1");
        assertEquals("1", list.get(0));
    }

    @Test
    void containsAll() {
        LLinkedList<String> list = new LLinkedList<>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");

        assertTrue(list.containsAll(List.of("1", "2", "7")));
        assertFalse(list.containsAll(List.of("1", "2", "9")));
    }

    @Test
    void addAll() {

        LLinkedList<String> list = new LLinkedList<>();

        list.addAll(List.of(NUMBERS));

        ArrayList<String> arrayList = new ArrayList<>();

        for (String s : list) {
            arrayList.add(s);
        }

        assertIterableEquals(List.of(NUMBERS), arrayList);
    }

    @Test
    void clear() {
        LLinkedList<String> list = new LLinkedList<>();

        list.addAll(List.of(NUMBERS));
        list.clear();
        assertTrue(list.isEmpty());
    }

    @Test
    void get() {
        LLinkedList<String> list = new LLinkedList<>();

        list.addAll(List.of(NUMBERS));

        assertEquals(list.get(3), "4");
    }

    @Test
    void set() {
        LLinkedList<String> list = new LLinkedList<>();

        list.addAll(List.of(NUMBERS));

        list.set(3, "10");
        assertEquals(list.get(3), "10");
    }
}