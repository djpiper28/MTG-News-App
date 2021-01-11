package djpiper28.mtgnewsapp.setpreview.cardpreview;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import forohfor.scryfall.api.Card;

public class CardSearchAndSorter {

    private static List<Card> cache;
    private static String query;
    private static SortBy sortBy;
    private static String type;
    private static String colour;
    private static Random random = new Random();

    private CardSearchAndSorter() {
        // Utils class
    }

    public static void resetCache() {
        cache = null;
    }

    private static boolean isCached(String query, SortBy sortBy, String type, String colour) {
        return cache != null && (query.equals(CardSearchAndSorter.query) && sortBy.equals(CardSearchAndSorter.sortBy)
                && type.equals(CardSearchAndSorter.type) && colour.equals(CardSearchAndSorter.colour));
    }

    private static String alphaNum(String str) {
        StringBuilder output = new StringBuilder();

        for (String character : str.toLowerCase().split("")) {
            if (character.matches("[a-zA-Z 0-9-]*")) {
                output.append(character);
            }
        }

        return output.toString();
    }

    private static List<Card> getSearchResults(List<Card> cards, String query) {
        List<Card> output = new LinkedList<>();
        query = ".*" + alphaNum(query).replace(" ", ".*") + ".*";

        for (Card card : cards) {
            if (alphaNum(card.getName()).matches(query) || alphaNum(card.getOracleText()).matches(query)) {
                output.add(card);
            }
        }

        return output;
    }

    private static List<Card> randomOrder(List<Card> cards) {
        List<Card> output = new LinkedList<>();

        for (Card card : cards) {
            output.add(random.nextInt(output.size() == 0 ? 1 : output.size()), card);
        }

        return output;
    }

    private static List<Card> sortCardsBy(List<Card> cards, SortBy sortBy) {
        if (sortBy == SortBy.RANDOM) {
            return randomOrder(cards);
        } else {
            return sortCardsByR(cards, sortBy);
        }
    }

    private static List<Card> sortCardsByR(List<Card> cards, SortBy sortBy) {
        if (cards.size() <= 1) {
            return cards;
        }

        Card pivot = cards.remove(cards.size() / 2);

        List<Card> a = new LinkedList<>();
        List<Card> b = new LinkedList<>();

        for (Card card : cards) {
            try {
                switch (sortBy) {
                    case CMC:
                        if (card.getCmc() < pivot.getCmc()) {
                            a.add(card);
                        } else {
                            b.add(card);
                        }
                        break;
                    case NAME:
                        if (card.getName().compareTo(pivot.getName()) <= 0) {
                            a.add(card);
                        } else {
                            b.add(card);
                        }
                        break;
                    case POWER:
                        if (Integer.parseInt(card.getPower()) < Integer.parseInt(pivot.getPower())) {
                            a.add(card);
                        } else {
                            b.add(card);
                        }
                        break;
                    case TOUGHNESS:
                        if (Integer.parseInt(card.getToughness()) < Integer.parseInt(pivot.getToughness())) {
                            a.add(card);
                        } else {
                            b.add(card);
                        }
                        break;
                    case COLLECTORS_NUMBER:
                        if (card.getCollectorNumber().compareTo(pivot.getCollectorNumber()) <= 0) {
                            a.add(card);
                        } else {
                            b.add(card);
                        }
                        break;
                }
            } catch (Exception e) {
                //Not int
                b.add(card);
            }
        }

        a = sortCardsByR(a, sortBy);
        b = sortCardsByR(b, sortBy);

        List<Card> output = new LinkedList<>(a);
        output.add(pivot);
        output.addAll(b);

        return output;
    }

    private static List<Card> reverseSort(List<Card> cards) {
        List<Card> output = new LinkedList<>();

        for (int i = cards.size() - 1; i >= 0; i--) {
            output.add(cards.get(i));
        }

        return output;
    }

    /*
     * Set a parameter to "any" for any results to come back
     */
    private static List<Card> filter(List<Card> cards, String type, String colour) {
        if (type.equals("any") && colour.equals("Any")) {
            return cards;
        }

        List<Card> output = new LinkedList<>();

        for (Card card : cards) {
            if ((type.equals("Any") || card.getTypeLine().matches(".*" + type + ".*"))
                    && (colour.equals("Any") || card.getColorIdentity().contains(colour))) {
                output.add(card);
            }
        }

        return output;
    }

    public static List<Card> search(List<Card> cards, String query, String type, String colour, SortBy sortBy, boolean ascendingOrder) {
        if (isCached(query, sortBy, type, colour)) {
            return cache;
        }

        List<Card> output = sortCardsBy(getSearchResults(filter(cards, type, colour), query), sortBy);
        cache = output;
        return ascendingOrder ? output : reverseSort(output);
    }

    public static List<String> getSearchRecommendations(List<Card> cards, String query) {
        List<String> output = new LinkedList<>();
        String processedQuery = alphaNum(query).replace(" ", ".*");

        for (Card card : cards) {
            if (alphaNum(card.getName()).matches(processedQuery) && !query.equals(card.getName())) {
                output.add(card.getName());
            }
        }

        return output;
    }

}
