package mkk13.colorjudge;

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
}
