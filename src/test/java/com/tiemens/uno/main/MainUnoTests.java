package com.tiemens.uno.main;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        StringBufferPrintStream out = StringBufferPrintStream.getPrintStream();

        // Create a game with a fixed seed for reproducible results
        Random random = new Random(randomSeed); // Change seed for different games
        UnoGame game = new UnoGame(random, out);
        game.playGame(out);

        Assertions.assertNotNull(game.getWinner(), "game is null");
        Assertions.assertEquals(59, game.getRoundNumber(), "wrong number of rounds");

	}
}
