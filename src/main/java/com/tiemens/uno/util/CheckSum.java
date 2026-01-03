package com.tiemens.uno.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.tiemens.uno.model.Card;

public class CheckSum {

    public enum Keys {
        After_deck_init("1_"), After_shuffle("2_"), After_deal("3_"), After_topCard_set("4_");
        private final String prepend;
        private Keys(String prepend) {
            this.prepend = prepend;
        }
        @Override
        public String toString() {
            return prepend + super.toString();
        }
    }

    public static Map<String, String> map = new TreeMap<>();

    public static String createString(List<Card> list) {
        String joined = list.stream()
                .map(Card::toString) // Map each object to its string
                .collect(Collectors.joining(", "));

        joined = list.size() + ", " + joined;
        return joined;
    }

    public static void record(Keys key, List<Card> deck) {
        record(key.toString(), deck);
    }

    public static void record(String key, List<Card> deck) {
        String checkstring = createString(deck);
        map.put(key, checkstring);
    }

    @Override
    public String toString() {
        return toStaticString();
    }

    public static String toStaticString() {
        String newline = "\n";
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb.append(key + ", ");
            sb.append(map.get(key));
            sb.append(newline);
        }
        return sb.toString();
    }
}
