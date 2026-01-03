package com.tiemens.uno.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.tiemens.uno.model.Card.Color;

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

    public Card findPlayableCard(Card topCard, Random random, PrintStream out) {
        List<Card> playableCards = new ArrayList<>();
        for (Card card : hand) {
            if (card.canPlayOn(topCard)) {
                playableCards.add(card);
            }
        }

        if (playableCards.isEmpty()) {
            return null;
        }

        final int pick = random.nextInt(playableCards.size());
        if (playableCards.size() > 1) {
            out.println("Pick Card: There was a choice of playable cards, picked " + pick + " out of " + playableCards.size());
        }

        // Return a random playable card
        return playableCards.get(pick);
    }

    public Card.Color chooseColor(Random random, PrintStream out) {
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
            final int pick = random.nextInt(colors.length);
            out.println("Pick Color: There was no majority color, pick=" + pick);
            return colors[pick];
        }

        // "bug": when there is a tie, there is an uncertainty introduced.
        //        sometimes it picks the first, other times the second
        final Card.Color pick = getMaxValueThenByLowestKey(colorCount);
        out.println("Pick Color: Max color(" + pick.toString() + ") was " + colorCount.get(pick) + " all=" + colorCount);
        // Return the most frequent color
        return pick;
    }

    private static List<Card.Color> ORDER = new ArrayList<>();
    static {
        ORDER.add(Color.BLUE);
        ORDER.add(Color.GREEN);
        ORDER.add(Color.RED);
        ORDER.add(Color.YELLOW);
    }

    private Color getMaxValueThenByLowestKey(Map<Color, Integer> colorCount) {
        int max = getMaxValue(colorCount);
        Card.Color matches = findMatchesWithLowestKey(ORDER, colorCount, max);
        return matches;
    }

    private Color findMatchesWithLowestKey(List<Color> checkOrder, Map<Color, Integer> colorCount, int max) {
        for (Color c : checkOrder) {
            // check in that specific order:
            if (colorCount.containsKey(c) && colorCount.get(c).equals(max)) {
                return c;
            }
        }
        return null;
    }

    private int getMaxValue(Map<Card.Color, Integer> colorCount) {
        int max = Integer.MIN_VALUE;
        for (Map.Entry<Card.Color, Integer> entry : colorCount.entrySet()) {
            if (entry.getValue().intValue() > max) {
                max = entry.getValue().intValue();
            }
        }
        return max;
    }
}