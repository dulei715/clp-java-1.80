package edu.ecnu.dll.tools.collection;

import java.util.Comparator;

public class ArrayFirstElementDescendComparator implements Comparator<double[]> {

    @Override
    public int compare(double[] elemA, double[] elemB) {
        double diff = elemA[0] - elemB[0];
        if (diff > 0.0)   return -1;
        else if (diff == 0.0)     return 0;
        else    return 1;
    }
}
