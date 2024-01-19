package com.finance.treasuretracker.view.tabs.utils;

public final class ArrayUtils {

    // Private constructor to prevent instantiation
    private ArrayUtils() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Adds a new element to a String array.
     *
     * @param originalArray the original array
     * @param newElement    the element to be added
     * @return a new array containing all elements of the original array and the new element
     */
    public static String[] addToStringArray(String[] originalArray, String newElement) {
        String[] newArray = new String[originalArray.length + 1];
        System.arraycopy(originalArray, 0, newArray, 0, originalArray.length);
        newArray[originalArray.length] = newElement;
        return newArray;
    }
}
