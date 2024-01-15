package com.finance.treasuretracker.view.tabs.utils;

public class ComboBoxItem<T> {
    private T item;
    private String name;

    public ComboBoxItem(T item, String name) {
        this.item = item;
        this.name = name;
    }

    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        return name;
    }
}

