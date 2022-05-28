package edu.ecnu.dll.tools.collection;

import java.util.Set;

public class SetUtils {
    public static void addSeriesNatualNumber(Set<Integer> set, Integer fromValue, Integer endValue) {
        for (int i = fromValue; i <= endValue; i++) {
            set.add(i);
        }
    }
}
