package edu.ecnu.dll;

import edu.ecnu.dll.cpl.struct.ExtendDouble;
import org.junit.Test;

public class BasicTest {
    @Test
    public void fun1() {
        Double vA = Double.MAX_VALUE;
        System.out.println(vA);
        Double vB = Double.MAX_VALUE * 2;
        System.out.println(vB);
        Double vC = Double.MAX_VALUE * 4;
        System.out.println(vB);
        System.out.println(vA < vB);
        System.out.println(vB < vC);
    }

    @Test
    public void fun2() {
        ExtendDouble vA = new ExtendDouble(2.0,3.0);
        ExtendDouble vB = new ExtendDouble(2.0,3.0);

        System.out.println(vA.compareTo(vB));

        ExtendDouble vC = new ExtendDouble(2.0,4.0);
        System.out.println(vB.compareTo(vC));

        ExtendDouble vD = new ExtendDouble(2.0,2.0);
        System.out.println(vB.compareTo(vD));

        ExtendDouble vE = new ExtendDouble(3.0,2.0);
        System.out.println(vB.compareTo(vE));
    }

}
