package mkk13.colorjudge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by mkk-1 on 31/03/2017.
 */

public class Utils {
    public static final Integer COLORS_IN_LIST = 13;
    public static final int IMAGE_CAPTURE_INTENT = 1;
    public static final int IMAGE_GALLERY_INTENT = 2;
    public static final int IMAGE_CROP_INTENT = 3;
    public static final int SOUND_CAPTURE_INTENT = 4;
    public static Language LANGUAGE = Language.POLISH;

    public enum Language {
        ENGLISH,
        POLISH;

        @Override
        public String toString() {
            return super.toString();
        }

        public String getLocale() {
            switch(this) {
                case POLISH:
                    return "pl-PL";

                default:
                case ENGLISH:
                    return "en-US";
            }
        }
    }

    /*
     * Fisher - Yates shuffle
     *
     * @param array    Array to shuffle
     * @param nb_picks Number of shuffled elements to return
     */
    public static List<Color> shuffle(List<Color> array, int nb_picks) {
        List<Color> res = new ArrayList<Color>(array);
        Collections.shuffle(res);
        return res.subList(0, nb_picks);
    }

    public static List<Color> shuffle(List<Color> array) {
        return shuffle(array, array.size());
    }

    public static int shuffleSetColors(Color testColor, List<Color> array) {
        // Reshuffle our 4 colors
        List<Color> colArray = shuffle(array);

        // For each color...
        for (int i = 0; i < colArray.size(); i++)
        {
            if (colArray.get(i) == testColor)
                return i;
        }

        return -1;
    }

    public static Pair<Integer, Color> ShuffleColors(List<Color> colors) {
        // Shuffle colors and pick an array of 4
        List<Color> colArray = shuffle(colors, 4);

        // First color will be the 'test color'
        Color primaryColor = colArray.get(0);

        // Shuffle and set appropriate nodes
        return new Pair<>(shuffleSetColors(primaryColor, colArray), primaryColor);
    }

    public static Pair<Integer, Color> randomCloseColors(List<Color> array, Map<String, List<Color>> colorsMap) {
        // Randomize single color and get it's category
        String subCategory = "";
        int num = (int) (Math.random() * array.size());
        for(Color col : array) {
            if (--num < 0) {
                subCategory = col.getBase();
            }
        }

        // Shuffle previously chosen category and pick an array of 4
        List<Color> colArray = shuffle(colorsMap.get(subCategory), 4);

        // First color will be the 'test color'
        Color primaryColor = colArray.get(0);

        // Shuffle and set appropriate nodes
        return new Pair<>(shuffleSetColors(primaryColor, colArray), primaryColor);
    }


}
