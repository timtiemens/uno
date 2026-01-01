package com.tiemens.uno.main;

import java.util.Random;

import com.tiemens.uno.model.UnoGame;
import com.tiemens.uno.util.StringBufferPrintStream;


// Main class to run the game
public class MainUno {
    public static class MainUnoStats {
        public int total;
        public int winner;
        public int noWinner;

        public MainUnoStats() {

        }
        private void incTotal() {
            total += 1;
        }
        public void incWinner() {
            incTotal();
            winner += 1;
        }
        public void incNoWinner() {
            incTotal();
            noWinner += 1;
        }
        @Override
        public String toString() {
            return "MainUnoStats [total=" + total + ", winner=" + winner + ", noWinner=" + noWinner + "]";
        }

    }
    public static void main(String[] args) {
        // number of games to simulate
        final int numberGames = 2230;
        // initial random:
        int randomSeed = 42;
        StringBufferPrintStream out = StringBufferPrintStream.getPrintStream();

        int addToSeed = 10;

        MainUnoStats mus = new MainUnoStats();
        for (int i = 0; i < numberGames; i++) {
           // Create a game with a fixed seed for reproducible results
           Random random = new Random(randomSeed); // Change seed for different games
           UnoGame game = new UnoGame(random, out);
           game.playGame(out);
           if (game.getWinner() != null) {
               System.out.println("Winner " + game.getWinner().getName() + " recorded, number of rounds=" + game.getRoundNumber());
               mus.incWinner();
           } else {
              System.out.println("No winner recorded");
              mus.incNoWinner();
           }
           randomSeed += addToSeed;
           out.clear();
        }
        System.out.println("Stats = " + mus);

        //System.out.println("SBPS.size=" + out.size());
        //out.clear();
        //System.out.println("SBPS.size=" + out.size());
    }
}
