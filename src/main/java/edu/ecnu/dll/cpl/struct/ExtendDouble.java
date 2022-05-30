package edu.ecnu.dll.cpl.struct;

public class ExtendDouble implements Comparable<ExtendDouble> {
    public Double coefficientOfM = null;
    public Double constant = null;

    public ExtendDouble(Double coefficientOfM, Double constant) {
        this.coefficientOfM = coefficientOfM;
        this.constant = constant;
    }

    public static ExtendDouble valueOf(Double value) {
        return new ExtendDouble(0.0, value);
    }


    @Override
    public int compareTo(ExtendDouble elem) {
        if (!this.coefficientOfM.equals(elem.coefficientOfM)) {
            return this.coefficientOfM.compareTo(elem.coefficientOfM);
        }
        return this.constant.compareTo(elem.constant);
    }
}
