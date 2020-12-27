package djpiper28.mtgnewsapp.setpreview.cardpreview;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.LinkedList;
import java.util.List;

import djpiper28.mtgnewsapp.R;
import forohfor.scryfall.api.Card;

public class Manamoji {

    private Manamoji() {
        // Utils class
    }

    public static List<String> getKeys(Card card) {
        char[] cost = card.getManaCost().toCharArray();
        List<String> output = new LinkedList<>();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cost.length; i++) {
            stringBuilder.append(cost[i]);

            if (cost[i] == '}') {
                output.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }
        }

        return output;
    }

    public static List<Drawable> getManamojis(List<String> keys, Context context) {
        List<Drawable> drawables = new LinkedList<>();

        for (String key : keys) {
            drawables.add(getManamoji(key, context));
        }

        return drawables;
    }

    public static Drawable getManamoji(String key, Context context) {
        switch ("mana" + key.toLowerCase().replace("/", "").replace("{", "").replace("}", "")) {
            case "mana19":
                return context.getResources().getDrawable(R.drawable.mana19);
            case "manaz":
                return context.getResources().getDrawable(R.drawable.manaz);
            case "mana100":
                return context.getResources().getDrawable(R.drawable.mana100);
            case "manahw":
                return context.getResources().getDrawable(R.drawable.manahw);
            case "manay":
                return context.getResources().getDrawable(R.drawable.manay);
            case "mana17":
                return context.getResources().getDrawable(R.drawable.mana17);
            case "mana1000000":
                return context.getResources().getDrawable(R.drawable.mana1000000);
            case "manahr":
                return context.getResources().getDrawable(R.drawable.manahr);
            case "manap":
                return context.getResources().getDrawable(R.drawable.manap);
            case "manainfinity":
                return context.getResources().getDrawable(R.drawable.manainfinity);
            case "mana18":
                return context.getResources().getDrawable(R.drawable.mana18);
            case "manahalf":
                return context.getResources().getDrawable(R.drawable.manahalf);
            case "mana14":
                return context.getResources().getDrawable(R.drawable.mana14);
            case "managu":
                return context.getResources().getDrawable(R.drawable.managu);
            case "mana5":
                return context.getResources().getDrawable(R.drawable.mana5);
            case "manas":
                return context.getResources().getDrawable(R.drawable.manas);
            case "manawb":
                return context.getResources().getDrawable(R.drawable.manawb);
            case "mana2b":
                return context.getResources().getDrawable(R.drawable.mana2b);
            case "manachaos":
                return context.getResources().getDrawable(R.drawable.manachaos);
            case "mana8":
                return context.getResources().getDrawable(R.drawable.mana8);
            case "manaw":
                return context.getResources().getDrawable(R.drawable.manaw);
            case "manaq":
                return context.getResources().getDrawable(R.drawable.manaq);
            case "managw":
                return context.getResources().getDrawable(R.drawable.managw);
            case "mana9":
                return context.getResources().getDrawable(R.drawable.mana9);
            case "mana3":
                return context.getResources().getDrawable(R.drawable.mana3);
            case "manac":
                return context.getResources().getDrawable(R.drawable.manac);
            case "mana20":
                return context.getResources().getDrawable(R.drawable.mana20);
            case "manae":
                return context.getResources().getDrawable(R.drawable.manae);
            case "manarp":
                return context.getResources().getDrawable(R.drawable.manarp);
            case "manawu":
                return context.getResources().getDrawable(R.drawable.manawu);
            case "mana15":
                return context.getResources().getDrawable(R.drawable.mana15);
            case "manag":
                return context.getResources().getDrawable(R.drawable.manag);
            case "manar":
                return context.getResources().getDrawable(R.drawable.manar);
            case "mana16":
                return context.getResources().getDrawable(R.drawable.mana16);
            case "manaub":
                return context.getResources().getDrawable(R.drawable.manaub);
            case "manapw":
                return context.getResources().getDrawable(R.drawable.manapw);
            case "mana13":
                return context.getResources().getDrawable(R.drawable.mana13);
            case "manaup":
                return context.getResources().getDrawable(R.drawable.manaup);
            case "mana2r":
                return context.getResources().getDrawable(R.drawable.mana2r);
            case "mana0":
                return context.getResources().getDrawable(R.drawable.mana0);
            case "manaur":
                return context.getResources().getDrawable(R.drawable.manaur);
            case "manabp":
                return context.getResources().getDrawable(R.drawable.manabp);
            case "manabr":
                return context.getResources().getDrawable(R.drawable.manabr);
            case "manax":
                return context.getResources().getDrawable(R.drawable.manax);
            case "manawp":
                return context.getResources().getDrawable(R.drawable.manawp);
            case "manau":
                return context.getResources().getDrawable(R.drawable.manau);
            case "mana11":
                return context.getResources().getDrawable(R.drawable.mana11);
            case "mana7":
                return context.getResources().getDrawable(R.drawable.mana7);
            case "mana2":
                return context.getResources().getDrawable(R.drawable.mana2);
            case "mana6":
                return context.getResources().getDrawable(R.drawable.mana6);
            case "manaa":
                return context.getResources().getDrawable(R.drawable.manaa);
            case "manarw":
                return context.getResources().getDrawable(R.drawable.manarw);
            case "mana10":
                return context.getResources().getDrawable(R.drawable.mana10);
            case "manat":
                return context.getResources().getDrawable(R.drawable.manat);
            case "mana1":
                return context.getResources().getDrawable(R.drawable.mana1);
            case "manab":
                return context.getResources().getDrawable(R.drawable.manab);
            case "mana2w":
                return context.getResources().getDrawable(R.drawable.mana2w);
            case "mana2g":
                return context.getResources().getDrawable(R.drawable.mana2g);
            case "mana4":
                return context.getResources().getDrawable(R.drawable.mana4);
            case "mana2u":
                return context.getResources().getDrawable(R.drawable.mana2u);
            case "mana12":
                return context.getResources().getDrawable(R.drawable.mana12);
            case "managp":
                return context.getResources().getDrawable(R.drawable.managp);
            case "manarg":
                return context.getResources().getDrawable(R.drawable.manarg);
            case "manabg":
                return context.getResources().getDrawable(R.drawable.manabg);
            default:
                return context.getResources().getDrawable(R.drawable.mananotfound);
        }
    }

}
