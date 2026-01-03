package com.tiemens.uno.model;

import java.util.Objects;

/*   starting at random(42), with 4 players:
   Player 4 plays BLUE_7
   Player 4 wins!

*/
// Card class representing individual Uno cards
public class Card {
    public enum Color {
        BLUE, GREEN, RED, YELLOW, WILD;

    }

    public enum Type {
        NUMBER, SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR
    }

    private Color color;
    private Type type;
    private int number; // Only relevant for NUMBER cards

    public Card(Color color, Type type, int number) {
        this.color = color;
        this.type = type;
        this.number = number;
    }

    public Color getColor() { return color; }
    public Type getType() { return type; }
    public int getNumber() { return number; }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isWild() {
        return type == Type.WILD || type == Type.WILD_DRAW_FOUR;
    }

    public boolean canPlayOn(Card topCard) {
        if (this.isWild()) {
            return true;
        }
        if (topCard.isWild()) {
            return this.color == topCard.color;
        }
        return this.color == topCard.color ||
               (this.type == Type.NUMBER && topCard.type == Type.NUMBER && this.number == topCard.number) ||
               (this.type == topCard.type && this.type != Type.NUMBER);
    }

    @Override
    public String toString() {
        if (type == Type.NUMBER) {
            return color + "_" + number;
        } else {
            return color + "_" + type;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, number, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Card other = (Card) obj;
        return color == other.color && number == other.number && type == other.type;
    }


}