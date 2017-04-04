package mkk13.colorjudge;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by mkk-1 on 26/03/2017.
 */
public class Comparator{
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

    public static ArrayList<Score> findNamesPl(String searchName, Collection<Color> colors) {
        ArrayList<Score> searchScore = new ArrayList<>();
        for (Color i : colors) {
            searchScore.add(new Score(i, jaroWinklerDistanceInt(searchName, i.name_pl)));
        }
        Collections.sort(searchScore, Collections.reverseOrder());
        return searchScore;
    }

    public static ArrayList<Score> findNamesEng(String searchName, Collection<Color> colors) {
        ArrayList<Score> searchScore = new ArrayList<>();
        for (Color i : colors) {
            searchScore.add(new Score(i, jaroWinklerDistanceInt(searchName, i.name_eng)));
        }
        Collections.sort(searchScore, Collections.reverseOrder());
        return searchScore;
    }

    public static ArrayList<Score> findColors(Color searchColor, Collection<Color> colors) {
        ArrayList<Score> searchScore = new ArrayList<>();
        for (Color i : colors) {
            searchScore.add(new Score(i, deltaEInt(searchColor.lab, i.lab)));
        }
        Collections.sort(searchScore, Collections.reverseOrder());
        return searchScore;
    }

    public static ArrayList<Score> getColors(Collection<Color> colors) {
        ArrayList<Score> searchScore = new ArrayList<>();
        int counter = 0;
        for (Color i : colors) {
            searchScore.add(new Score(i, counter++));
        }
        Collections.sort(searchScore);
        return searchScore;
    }
}
