package mkk13.colorjudge;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mkk-1 on 26/03/2017.
 */
public class ColorUtils {
    private static final double deltaEMax = Math.sqrt(Math.pow(100D, 2D) + Math.pow(255D, 2D) + Math.pow(255D, 2D));

    private static int jaroWinklerDistanceInt(String stringA, String stringB) {
        return (int)(100 * StringUtils.getJaroWinklerDistance(stringA, stringB));
    }

    private static int deltaEInt(float[] lab1, float[] lab2) {
        return 100 - (int)(100 * (deltaE(lab1, lab2) / deltaEMax));
    }

    private static double deltaE(float[] lab1, float[] lab2) {
        if (lab1.length != 3 || lab2.length != 3) {
            return Double.MAX_VALUE;
        }
        double res = 0;
        for (int i = 0; i < lab1.length; i++) {
            res += Math.pow(lab2[i] - lab1[i], 2.0);
        }
        return Math.sqrt(res);
    }

    public static ArrayList<Score> getMatchingNames(String searchName, Collection<Color> colors) {
        ArrayList<Score> searchScore = new ArrayList<>();
        for (Color i : colors) {
            searchScore.add(new Score(i, jaroWinklerDistanceInt(searchName, i.getName())));
        }
        Collections.sort(searchScore, Collections.reverseOrder());
        return searchScore;
    }

    public static ArrayList<Score> getMatchingColors(Color searchColor, Collection<Color> colors) {
        ArrayList<Score> searchScore = new ArrayList<>();
        for (Color i : colors) {
            searchScore.add(new Score(i, deltaEInt(searchColor.getLAB(), i.getLAB())));
        }
        Collections.sort(searchScore, Collections.reverseOrder());
        return searchScore;
    }

    public static ArrayList<Score> getSortedColors(Collection<Color> colors) {
        ArrayList<Score> searchScore = new ArrayList<>();

        for (Color col : colors) {
            searchScore.add(new Score(col));
        }
        Collections.sort(searchScore, Score.byNameComparator);
        return searchScore;
    }
}
