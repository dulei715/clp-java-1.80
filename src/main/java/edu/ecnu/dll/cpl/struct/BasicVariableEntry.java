package edu.ecnu.dll.cpl.struct;

import edu.ecnu.dll.tools.collection.ArraysUtils;

import java.util.Map;
import java.util.Objects;

public class BasicVariableEntry implements Map.Entry<Integer, ExtendDouble>{
    private Integer key = null;
    private ExtendDouble value = null;

    public BasicVariableEntry(Integer key, ExtendDouble value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer getKey() {
        return this.key;
    }

    @Override
    public ExtendDouble getValue() {
        return this.value;
    }

    @Override
    public ExtendDouble setValue(ExtendDouble value) {
        this.value = value;
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicVariableEntry that = (BasicVariableEntry) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }


    public static void initializeExtendDoubleArray(Map.Entry<Integer, ExtendDouble>[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new BasicVariableEntry(i, ExtendDouble.valueOf(0.0));
        }
    }


    public static boolean containKey(Map.Entry<Integer, ExtendDouble>[] arr, Integer key) {
        if (ArraysUtils.findIndexOfGivenKey(arr, key) < 0) {
            return false;
        }
        return true;
    }

}
