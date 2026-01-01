package com.tiemens.uno.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//Player class representing a game participant
public class Player {
    private String name;
    private List<Card> hand = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getHandSize() {
        return hand.size();
    }

    public void drawCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public Card findPlayableCard(Card topCard, Random random) {
        List<Card> playableCards = new ArrayList<>();
        for (Card card : hand) {
            if (card.canPlayOn(topCard)) {
                playableCards.add(card);
            }
        }

        if (playableCards.isEmpty()) {
            return null;
        }

        // Return a random playable card
        return playableCards.get(random.nextInt(playableCards.size()));
    }

    public Card.Color chooseColor(Random random) {
        // Choose based on most common color in hand
        Map<Card.Color, Integer> colorCount = new HashMap<>();
        for (Card card : hand) {
            if (!card.isWild()) {
                colorCount.put(card.getColor(), colorCount.getOrDefault(card.getColor(), 0) + 1);
            }
        }

        if (colorCount.isEmpty()) {
            // If no colored cards, choose randomly
            Card.Color[] colors = { Card.Color.RED, Card.Color.GREEN, Card.Color.BLUE, Card.Color.YELLOW };
            return colors[random.nextInt(colors.length)];
        }

        // Return the most frequent color
        return Collections.max(colorCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}