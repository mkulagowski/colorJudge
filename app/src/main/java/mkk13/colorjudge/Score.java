package mkk13.colorjudge;

/**
 * Created by mkk-1 on 26/03/2017.
 */
public class Score implements Comparable<Score>{
    public Color col;
    public int score;

    public Score(Color col, int score) {
        this.col = col;
        this.score = score;
    }

    public int compareTo(Score sc) {
        return score - sc.score;
    }
}
