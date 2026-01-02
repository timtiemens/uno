package com.tiemens.uno.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.tiemens.uno.util.StringBufferPrintStream;

// Main game class
public class UnoGame {
    private List<Player> players;
    private List<Card> deck;
    private Card topCard;
    private int currentPlayerIndex;
    private Direction direction; // 1 for clockwise, -1 for counter-clockwise
    private final int randomSeed;
    private Random random;
    private boolean gameOver;
    private Player winner;
    private int roundNumber;
    private String reason; // reason the game ended

    public UnoGame(int randomSeed, PrintStream out) {
        if (out == null) {
            out = StringBufferPrintStream.NO_OP_PRINTSTREAM;
        }
        this.randomSeed = randomSeed;
        this.random = new Random(randomSeed);
        this.players = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.direction = Direction.CLOCKWISE;
        this.gameOver = false;
        this.roundNumber = 0;
        this.reason = "";
        initializePlayers();
        initializeDeck();
        shuffleDeck();
        dealCards();
        placeFirstCard(out);
    }

    private void initializePlayers() {
        for (int i = 0; i < 4; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
    }

    private void initializeDeck() {
        // Add number cards (0-9) for each color
        for (Card.Color color : Arrays.asList(Card.Color.RED, Card.Color.GREEN, Card.Color.BLUE, Card.Color.YELLOW)) {
            // One 0 card
            deck.add(new Card(color, Card.Type.NUMBER, 0));

            // Two of each 1-9
            for (int i = 1; i <= 9; i++) {
                deck.add(new Card(color, Card.Type.NUMBER, i));
                deck.add(new Card(color, Card.Type.NUMBER, i));
            }

            // Two of each special card
            for (int i = 0; i < 2; i++) {
                deck.add(new Card(color, Card.Type.SKIP, 0));
                deck.add(new Card(color, Card.Type.REVERSE, 0));
                deck.add(new Card(color, Card.Type.DRAW_TWO, 0));
            }
        }

        // Add wild cards
        for (int i = 0; i < 4; i++) {
            deck.add(new Card(Card.Color.WILD, Card.Type.WILD, 0));
            deck.add(new Card(Card.Color.WILD, Card.Type.WILD_DRAW_FOUR, 0));
        }
        // System.out.println("Deck size in init is " + deck.size()); // 108
    }

    private void shuffleDeck() {
        Collections.shuffle(deck, random);
    }

    private void dealCards() {
        System.out.println("enter deal, deck size is " + deck.size()); // 108
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.drawCard(deck.remove(deck.size() - 1));
            }
        }
        // System.out.println("Deck size after deal is " + deck.size()); // 80
    }

    private void placeFirstCard(PrintStream out) {
        do {
            if (topCard != null) {
                out.println("Discarding first card of " + topCard);
            }
            topCard = deck.remove(deck.size() - 1);
        } while (topCard.isWild());

        // Handle initial special cards
        if (topCard.getType() == Card.Type.SKIP) {
            currentPlayerIndex = getNextCurrentPlayerIndex(currentPlayerIndex);
        } else if (topCard.getType() == Card.Type.REVERSE) {
            direction = direction.getReverseDirection();
        } else if (topCard.getType() == Card.Type.DRAW_TWO) {
            Player nextPlayer = getNextPlayer();
            nextPlayer.drawCard(deck.remove(deck.size() - 1));
            nextPlayer.drawCard(deck.remove(deck.size() - 1));
            currentPlayerIndex = getNextCurrentPlayerIndex(currentPlayerIndex);
        }

        // System.out.println("Deck size after place first is " + deck.size());
    }



    public void playGame(PrintStream out) {
        out.println("Starting Uno Game");
        out.println("First card: " + topCard);

        while (!gameOver) {
            roundNumber++;
            Player currentPlayer = players.get(currentPlayerIndex);
            out.println("\n" + currentPlayer.getName() + "'s turn, round " + roundNumber);
            out.println("Top card: " + topCard);

            Card playedCard = currentPlayer.findPlayableCard(topCard, random);

            if (playedCard != null) {
                currentPlayer.removeCard(playedCard);
                out.println(currentPlayer.getName() + " plays " + playedCard);

                // Handle wild cards
                if (playedCard.isWild()) {
                    Card.Color chosenColor = currentPlayer.chooseColor(random);
                    playedCard.setColor(chosenColor);
                    out.println(currentPlayer.getName() + " chooses " + chosenColor);
                }

                topCard = playedCard;
                handleSpecialCard(playedCard, out);

                if (currentPlayer.getHandSize() == 0) {
                    winner = currentPlayer;
                    gameOver = true;
                    reason = currentPlayer.getName() + " wins";
                    out.println("\n" + currentPlayer.getName() + " wins!");
                    break;
                }
            } else {
                // Player must draw a card
                out.println(currentPlayer.getName() + " draws a card");
                if (deck.isEmpty()) {
                    out.println("Deck is empty! Game ends in a draw.");
                    gameOver = true;
                    reason = currentPlayer.getName() + ", the deck is empty";
                    break;
                }
                Card drawnCard = deck.remove(deck.size() - 1);
                currentPlayer.drawCard(drawnCard);

                // Check if drawn card can be played
                if (drawnCard.canPlayOn(topCard)) {
                    currentPlayer.removeCard(drawnCard);
                    out.println(currentPlayer.getName() + " plays drawn card: " + drawnCard);

                    // Handle wild cards
                    if (drawnCard.isWild()) {
                        Card.Color chosenColor = currentPlayer.chooseColor(random);
                        drawnCard.setColor(chosenColor);
                        out.println(currentPlayer.getName() + " chooses " + chosenColor);
                    }

                    topCard = drawnCard;
                    handleSpecialCard(drawnCard, out);

                    if (currentPlayer.getHandSize() == 0) {
                        winner = currentPlayer;
                        gameOver = true;
                        reason = currentPlayer.getName() + " wins";
                        out.println("\n" + currentPlayer.getName() + " wins!");
                        break;
                    }
                }
            }

            // Move to next player
            currentPlayerIndex = getNextCurrentPlayerIndex(currentPlayerIndex);
        }
    }

    private void handleSpecialCard(Card card, PrintStream out) {
        switch (card.getType()) {
            case SKIP:
                currentPlayerIndex = getNextCurrentPlayerIndex(currentPlayerIndex);
                out.println("Next player is skipped!");
                break;
            case REVERSE:
                direction = direction.getReverseDirection();
                out.println("Direction reversed!");
                break;
            case DRAW_TWO:
                Player nextPlayer = getNextPlayer();
                out.println(nextPlayer.getName() + " draws 2 cards");
                if (deck.size() < 2) {
                    out.println("Not enough cards to draw! Skipping penalty.");
                    break;
                }
                nextPlayer.drawCard(deck.remove(deck.size() - 1));
                nextPlayer.drawCard(deck.remove(deck.size() - 1));
                currentPlayerIndex = getNextCurrentPlayerIndex(currentPlayerIndex);
                break;
            case WILD_DRAW_FOUR:
                Player nextPlayerWD4 = getNextPlayer();
                out.println(nextPlayerWD4.getName() + " draws 4 cards");
                if (deck.size() < 4) {
                    out.println("Not enough cards to draw! Skipping penalty.");
                    break;
                }
                for (int i = 0; i < 4; i++) {
                    nextPlayerWD4.drawCard(deck.remove(deck.size() - 1));
                }
                currentPlayerIndex = getNextCurrentPlayerIndex(currentPlayerIndex);
                break;
            default:
                break;
        }
    }

    private Player getNextPlayer() {
        int nextIndex = (currentPlayerIndex + direction.getAsInteger()) % players.size();
        if (nextIndex < 0) {
            nextIndex += players.size();
        }
        return players.get(nextIndex);
    }
    private int getNextCurrentPlayerIndex(int cpi) {
        int ret = (cpi + direction.getAsInteger()) % players.size();
        if (ret < 0) {
            ret += players.size();
        }
        return ret;
    }

    public Player getWinner() {
        return winner;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
    public String getReason() {
        return reason;
    }
    public int getRandomSeed() {
        return randomSeed;
    }
    public Card getTopCard() {
        return topCard;
    }
    public int getDeckSize() {
        return deck.size();
    }
}