package com.company;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by mkk-1 on 26/03/2017.
 */
public class JaroComparator{

    public static int compare(String stringA, String stringB) {
        return (int)(100 - (100 * StringUtils.getJaroWinklerDistance(stringA, stringB)));
    }
}
