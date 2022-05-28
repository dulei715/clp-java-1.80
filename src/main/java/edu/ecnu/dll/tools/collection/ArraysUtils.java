package edu.ecnu.dll.tools.collection;

import java.util.List;
import java.util.Set;

public class ArraysUtils {
    /**
     * @param a
     * @param fromIndex
     * @param toIndex
     * @param key
     * @return the minimal element whose value larger than key
     */
    public static int binaryDescendRangeSearchEqualRightFindLeft(double[] a, int fromIndex, int toIndex, double key) {
        int low = fromIndex;
        int high = toIndex - 1;
        int result = 0;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double midVal = a[mid];

            if (midVal > key)
                low = mid + 1;  // Neither val is NaN, thisVal is smaller
            else if (midVal < key)
                high = mid - 1; // Neither val is NaN, thisVal is larger
            else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(key);
                if (midBits == keyBits){
                    result =  mid - 1;             // Key found
                    if (result < 0) {
                        return -1;
                    }
                    return result;
                }else if (midBits > keyBits) // (-0.0, 0.0) or (!NaN, NaN)
                    low = mid + 1;
                else                        // (0.0, -0.0) or (NaN, !NaN)
                    high = mid - 1;
            }
        }
        result = low - 1;
        if (result < 0) {
            return -1;
        }
        return result;
    }

    public static int binaryDescendRangeSearchEqualRightFindLeft(double[] a, double key) {
        return binaryDescendRangeSearchEqualRightFindLeft(a, 0, a.length, key);
    }

    public static int binaryDescendRangeSearchEqualRightFindLeft(List<double[]> arrList, int fromIndex, int toIndex, double key) {
        int low = fromIndex;
        int high = toIndex - 1;
        int result = 0;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double[] midVal = arrList.get(mid);

            if (midVal[0] > key)
                low = mid + 1;  // Neither val is NaN, thisVal is smaller
            else if (midVal[0] < key)
                high = mid - 1; // Neither val is NaN, thisVal is larger
            else {
                long midBits = Double.doubleToLongBits(midVal[0]);
                long keyBits = Double.doubleToLongBits(key);
                if (midBits == keyBits){
                    result =  mid - 1;             // Key found
                    if (result < 0) {
                        return -1;
                    }
                    return result;
                }else if (midBits > keyBits) // (-0.0, 0.0) or (!NaN, NaN)
                    low = mid + 1;
                else                        // (0.0, -0.0) or (NaN, !NaN)
                    high = mid - 1;
            }
        }
        result = low - 1;
        if (result < 0) {
            return -1;
        }
        return result;
    }

    public static int binaryDescendRangeSearchEqualRightFindLeft(List<double[]> arrList, double key) {
        return binaryDescendRangeSearchEqualRightFindLeft(arrList, 0, arrList.size(), key);
    }

    /**
     *
     * @param arrList
     * @param fromIndex
     * @param toIndex
     * @param key
     * @return the minimal element whose value larger or equal than key
     */
    public static int binaryDescendRangeSearchEqualLeftFindLeft(List<double[]> arrList, int fromIndex, int toIndex, double key) {
        int low = fromIndex;
        int high = toIndex - 1;
        int result = 0;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double[] midVal = arrList.get(mid);

            if (midVal[0] > key)
                low = mid + 1;  // Neither val is NaN, thisVal is smaller
            else if (midVal[0] < key)
                high = mid - 1; // Neither val is NaN, thisVal is larger
            else {
                long midBits = Double.doubleToLongBits(midVal[0]);
                long keyBits = Double.doubleToLongBits(key);
                if (midBits == keyBits){
                    return mid;
                }else if (midBits > keyBits) // (-0.0, 0.0) or (!NaN, NaN)
                    low = mid + 1;
                else                        // (0.0, -0.0) or (NaN, !NaN)
                    high = mid - 1;
            }
        }
        result = low - 1;
        if (result < 0) {
            return -1;
        }
        return result;
    }

    public static int binaryDescendRangeSearchEqualLeftFindLeft(List<double[]> arrList, double key) {
        return binaryDescendRangeSearchEqualLeftFindLeft(arrList, 0, arrList.size(), key);
    }

    public static int getDoubleMaxValue(List<Double> list) {
        int resultIndex = -1;
        double resultValue =  - Double.MAX_VALUE;
        double temp;
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i);
            if (resultValue < temp) {
                resultValue = temp;
                resultIndex = i;
            }
        }
        return resultIndex;
    }

    public static Integer getDoubleMaxValueIndex(Double[] doubleArray) {
        int resultIndex = -1;
        double resultValue = - Double.MAX_VALUE;
        for (int i = 0; i < doubleArray.length; i++) {
            if (resultValue < doubleArray[i]) {
                resultValue = doubleArray[i];
                resultIndex = i;
            }
        }
        return resultIndex;
    }

    public static int getDoubleMinValueIndex(Double[] doubleArray) {
        int resultIndex = -1;
        double resultValue = Double.MAX_VALUE;
        for (int i = 0; i < doubleArray.length; i++) {
            if (resultValue > doubleArray[i]) {
                resultValue = doubleArray[i];
                resultIndex = i;
            }
        }
        return resultIndex;
    }

    public static Integer getDoubleMaxValueIndexInGivenIndexSet(Double[] arr, Set<Integer> givenIndexSet) {
        Double result = -Double.MAX_VALUE;
        Integer indexResult = -1;
        for (Integer index : givenIndexSet) {
            if (arr[index] > result) {
                indexResult = index;
                result = arr[index];
            }
        }
        return indexResult;
    }


    public static int getDoubleArrayMaxValueByFirstValue(List<double[]> list) {
        int resultIndex = -1;
        double resultValue =  - Double.MAX_VALUE;
        double temp;
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i)[0];
            if (resultValue < temp) {
                resultValue = temp;
                resultIndex = i;
            }
        }
        return resultIndex;
    }

    public static void setTwoDimensionalDoubleArray(Double[][] arr, double value) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = value;
            }
        }
    }

    public static int getFirstElementIndexWithValueMoreThanGivenValue(Double[] arr, int beginIndex, int endIndex, Double givenValue) {
        for (int i = beginIndex; i <= arr.length; i++) {
            if (arr[i] > givenValue) {
                return i;
            }
        }
        return -1;
    }

    public static int findIndexOfObject(Integer[] arr, Integer obj) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(obj)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        double[] arr = new double[] {
                9.0, 7.0, 6.0, 3.0, 1.0
        };

        double compDouble = 6.0;
        int result = binaryDescendRangeSearchEqualRightFindLeft(arr, compDouble);
        System.out.println(result);

    }

}
