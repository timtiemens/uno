package com.tiemens.uno.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.tiemens.uno.model.Card;
import com.tiemens.uno.model.UnoGame;
import com.tiemens.uno.util.StringBufferPrintStream;


class MainUnoTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test42() {
        // initial random:
        int randomSeed = 42;
        Card LAST_CARD = null;
        LAST_CARD = new Card(Card.Color.BLUE, Card.Type.NUMBER, 7);
        LAST_CARD = new Card(Card.Color.RED, Card.Type.NUMBER, 2);
// ALT        LAST_CARD = new Card(Card.Color.GREEN, Card.Type.NUMBER, 0);


        StringBufferPrintStream out = StringBufferPrintStream.getPrintStream();
        MainUno.MainUnoStats mus = new MainUno.MainUnoStats();

        // Create a game with a fixed seed for reproducible results
        UnoGame game = new UnoGame(randomSeed, out);

        Assertions.assertEquals(79, game.getDeckSize(), "wrong initial deck size");
        System.out.println("Starting game with seed=" + randomSeed + " and size=" + game.getDeckSize() + " cards.");

        game.playGame(out);
        mus.addStatsFromGame(game);
        System.out.println("MUS stats = " + mus.dumpDetails());

        Assertions.assertNotNull(game.getWinner(), "game is null");
        Assertions.assertEquals(randomSeed, game.getRandomSeed());
// ALT        Assertions.assertEquals(49, game.getDeckSize());
// ALT        Assertions.assertEquals(59, game.getRoundNumber(), "wrong number of rounds");
        Assertions.assertEquals(39, game.getDeckSize());
        Assertions.assertEquals(68, game.getRoundNumber(), "wrong number of rounds");
        Assertions.assertEquals(LAST_CARD, game.getTopCard());

	}
}
