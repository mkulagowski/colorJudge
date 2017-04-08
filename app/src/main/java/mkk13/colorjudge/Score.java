package mkk13.colorjudge;

import java.util.Comparator;

/**
 * Created by mkk-1 on 26/03/2017.
 */
public class Score implements Comparable<Score>{
    public Color mColor;
    public int mScore;

    public Score(Color col, int score) {
        this.mColor = col;
        this.mScore = score;
    }

    public Score(Color col) {
        this.mColor = col;
        this.mScore = 0;
    }

    public int compareTo(Score sc) {
        return mScore - sc.mScore;
    }

    public static final Comparator<Score> byNameComparator = new Comparator<Score>() {
        @Override
        public int compare(Score s1, Score s2) {
            return s1.mColor.getName().compareTo(s2.mColor.getName());
        }
    };
}
