package com.tiemens.uno.main;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;

import com.tiemens.uno.model.UnoGame;
import com.tiemens.uno.util.StringBufferPrintStream;


// Main class to run the game
public class MainUno {
    public static class MainUnoStats {
        public int total;
        private List<Integer> winner = new ArrayList<>();
        private List<Integer> noWinner = new ArrayList<>();
        private List<Integer> winnerSeeds = new ArrayList<>();

        private List<String> noWinnerReasons = new ArrayList<>();
        private List<Integer> noWinnerSeeds = new ArrayList<>();
        private List<UnoGame> noWinnerGames = new ArrayList<>();

        public MainUnoStats() {

        }
        private void incTotal() {
            total += 1;
        }
        public void incWinner(int numberRounds, int randomSeed) {
            incTotal();
            winner.add(numberRounds);
            winnerSeeds.add(randomSeed);
        }
        public void incNoWinner(int numberRounds, String reason, int randomSeed, UnoGame game) {
            incTotal();
            noWinner.add(numberRounds);
            noWinnerReasons.add(reason);
            noWinnerSeeds.add(randomSeed);
            noWinnerGames.add(game);
        }
        @Override
        public String toString() {
            return "MainUnoStats [total=" + total + ", winner=" + winner.size() + ", noWinner=" + noWinner.size() + "]";
        }
        public String dumpDetails() {
            final String newline = "\n";
            StringBuffer sb = new StringBuffer();

            sb.append("Uno game statistics" + newline);
            sb.append(" Total=   " + (winner.size() + noWinner.size()) + newline);
            sb.append(" Winner=  " + winner.size() + newline);
            sb.append(" NoWinner=" + noWinner.size() + newline);
            sb.append("  Winner rounds=" + winner + newline);
            sb.append("  Winner seeds =" + winnerSeeds + newline);
            sb.append("  NoWinner rounds=" + noWinner + newline);
            sb.append("  NoWinner seeds=" + noWinnerSeeds + newline);

            IntSummaryStatistics stats = winner.stream()
                    .mapToInt(Integer::intValue)
                    .summaryStatistics();
            sb.append("  Winner min: " + stats.getMin() + newline);
            sb.append("  Winner max: " + stats.getMax() + newline);
            sb.append("  Winner avg: " + stats.getAverage() + newline);

            for (String s : noWinnerReasons) {
                sb.append("   NoWinner reason: " + s + newline);
            }

            for (UnoGame game : noWinnerGames) {
                sb.append("   NoWinnerGame.topCard=" + game.getTopCard() + newline);
            }

            return sb.toString();
        }


        public void addStatsFromGame(UnoGame game) {
            int nRounds = game.getRoundNumber();
            if (game.getWinner() == null) {
                incNoWinner(nRounds, game.getReason(), game.getRandomSeed(), game);
            } else {
                incWinner(nRounds, game.getRandomSeed());
            }


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
           UnoGame game = new UnoGame(randomSeed, out);
           game.playGame(out);
           mus.addStatsFromGame(game);
           if (game.getWinner() != null) {
               System.out.println("Winner " + game.getWinner().getName() + " recorded, number of rounds=" + game.getRoundNumber());
           } else {
              System.out.println("No winner recorded");
           }
           randomSeed += addToSeed;
           out.clear();
        }
        System.out.println("Stats = " + mus);
        System.out.println(mus.dumpDetails());

        //System.out.println("SBPS.size=" + out.size());
        //out.clear();
        //System.out.println("SBPS.size=" + out.size());
    }
}
