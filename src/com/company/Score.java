package com.company;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by mkk-1 on 26/03/2017.
 */
public class Score implements Comparable<Score>{
    public String name;
    public int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int compareTo(Score sc) {
        return score - sc.score;
    }
}
