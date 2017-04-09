package mkk13.colorjudge;

import android.content.Context;
import android.content.res.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by mkk-1 on 31/03/2017.
 */

public class Utils {
    public static final Integer COLORS_IN_LIST = 13;
    public static final int IMAGE_CAPTURE_INTENT = 1;
    public static final int IMAGE_GALLERY_INTENT = 2;
    public static final int IMAGE_CROP_INTENT = 3;
    public static final int SOUND_CAPTURE_INTENT = 4;
    public static Language LANGUAGE = Language.ENGLISH;

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

    public void changeLanguage(Context context, Language lang) {
        LANGUAGE = lang;

        Locale newLocale;
        switch (lang) {
            case POLISH:
                newLocale = new Locale("pl", "PL");
                break;
            default:
            case ENGLISH:
                newLocale = Locale.ENGLISH;
        }
        Locale.setDefault(newLocale);
        Configuration config = context.getResources().getConfiguration();
        config.locale = newLocale;
        context.getResources().updateConfiguration(config, null);
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
}
