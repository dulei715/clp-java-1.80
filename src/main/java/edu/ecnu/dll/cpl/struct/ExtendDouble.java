package edu.ecnu.dll.cpl.struct;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ExtendDouble implements Comparable<ExtendDouble> {
    public Double coefficientOfM = null;
    public Double constant = null;

    public ExtendDouble(Double coefficientOfM, Double constant) {
        this.coefficientOfM = coefficientOfM;
        this.constant = constant;
    }

    public ExtendDouble() {
        this.coefficientOfM = 0.0;
        this.constant = 0.0;
    }

    public static void initializeExtendDoubleArray(ExtendDouble[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new ExtendDouble();
        }
    }

    public static ExtendDouble valueOf(Double constantValue) {
        return new ExtendDouble(0.0, constantValue);
    }

    public ExtendDouble add(ExtendDouble value) {
        return new ExtendDouble(this.coefficientOfM + value.coefficientOfM, this.constant + value.constant);
    }

    public ExtendDouble add(Double value) {
        return new ExtendDouble(this.coefficientOfM, this.constant + value);
    }

    public ExtendDouble negative() {
        return new ExtendDouble(-this.coefficientOfM, -this.constant);
    }

    public ExtendDouble multiple(Double value) {
        return new ExtendDouble(this.coefficientOfM * value, this.constant * value);
    }

    public Double getConstant() {
        return constant;
    }

    public boolean isInfinity() {
        if (Math.abs(this.coefficientOfM) < Math.pow(10,-6)) {
            return false;
        }
        return true;
    }

    public static int getMaxValueIndex(ExtendDouble[] arr) {
        int index = 0;
        ExtendDouble maxValue = arr[0];
        int compareResult;
        for (int i = 1; i < arr.length; i++) {
            compareResult = maxValue.compareTo(arr[i]);
            if (compareResult < 0) {
                index = i;
                maxValue = arr[i];
            }
        }
        return index;
    }

    // todo: Wrong
    public static int getMaxValueIndexInGivenEntrySetWithLimitEndIndex(ExtendDouble[] arr, final Map.Entry<Integer, ExtendDouble>[] elementArr, final Set<Map.Entry<Integer, ExtendDouble>> givenSet, int endIndex) {
        // 找到第一个在该集合中的元素
        int i;
        ExtendDouble maxValue;
        int index, compareResult;
        int len = endIndex + 1;
        for (i = 0; i < len && !givenSet.contains(elementArr[i]); i++);
        if (i < len) {
            index = i;
            maxValue = arr[i];
            for (++i; i < len; i++) {
                compareResult = maxValue.compareTo(arr[i]);
                if (compareResult < 0 && givenSet.contains(elementArr[i])) {
                    index = i;
                    maxValue = arr[i];
                }
            }
            return index;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtendDouble that = (ExtendDouble) o;
        return Objects.equals(coefficientOfM, that.coefficientOfM) && Objects.equals(constant, that.constant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coefficientOfM, constant);
    }

    @Override
    public int compareTo(ExtendDouble elem) {
        if (!this.coefficientOfM.equals(elem.coefficientOfM)) {
            return this.coefficientOfM.compareTo(elem.coefficientOfM);
        }
        return this.constant.compareTo(elem.constant);
    }

    public int compareTo(Double elem) {
        if (Math.abs(this.coefficientOfM)>=Math.pow(10,-6)) {
            return this.coefficientOfM.compareTo(0.0);
        }
        return this.constant.compareTo(elem);
    }

}
