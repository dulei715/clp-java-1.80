package edu.ecnu.dll.tools.collection;

import edu.ecnu.dll.cpl.struct.BasicVariableEntry;
import edu.ecnu.dll.cpl.struct.ExtendDouble;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetUtils {
    public static void addSeriesNatualNumber(Set<Integer> set, Integer fromValue, Integer endValue) {
        for (int i = fromValue; i <= endValue; i++) {
            set.add(i);
        }
    }

    public static void addSeriesIndexOfGivenData(Set<Map.Entry<Integer, ExtendDouble>> set, final ExtendDouble[] data, Integer fromValue, Integer endValue) {
        for (int i = fromValue; i <= endValue; i++) {
            set.add(new BasicVariableEntry(i, data[i]));
        }
    }

    public static void addSeriesIndexOfGivenData(Set<Map.Entry<Integer, ExtendDouble>> set, final Map.Entry<Integer, ExtendDouble>[] data, Integer fromValue, Integer endValue) {
        for (int i = fromValue; i <= endValue; i++) {
            set.add(data[i]);
        }
    }
}
