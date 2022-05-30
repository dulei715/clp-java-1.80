package edu.ecnu.dll.cpl;

public class ConstrainType {
    public static final int LEQ = 0;
    public static final int GEQ = 1;
    public static final int EQ = 2;

    public static int reverse(int constrainType) {
        if (LEQ == constrainType) {
            return GEQ;
        }
        if (GEQ == constrainType) {
            return LEQ;
        }
        return EQ;
    }
}
