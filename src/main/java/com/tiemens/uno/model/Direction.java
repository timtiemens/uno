package com.tiemens.uno.model;

public enum Direction {
    CLOCKWISE(), COUNTERCLOCKWISE();

    public Direction getReverseDirection() {
        if (this == CLOCKWISE) {
            return COUNTERCLOCKWISE;
        } else {
            return CLOCKWISE;
        }
    }

    public int getAsInteger() {
        if (this == CLOCKWISE) {
            return 1;
        } else {
            return -1;
        }
    }

}