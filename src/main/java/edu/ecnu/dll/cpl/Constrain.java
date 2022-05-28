package edu.ecnu.dll.cpl;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Constrain {

    private TreeMap<Variable, Double> combinationMap = null;
    private Integer constrainType = null;
    private Double rightValue = null;

    public Constrain() {
        this.constrainType = ConstrainType.LEQ;
        this.combinationMap = new TreeMap<>();
        this.rightValue = 0.0;
    }

    public Constrain(TreeMap<Variable, Double> combinationMap, int constrainType, Double rightValue) {
        this.combinationMap = combinationMap;
        this.constrainType = constrainType;
        this.rightValue = rightValue;
    }

    public void setConstrainType(int constrainType) {
        this.constrainType = constrainType;
    }

    public TreeMap<Variable, Double> getCombinationMap() {
        return combinationMap;
    }

    public Integer getConstrainType() {
        return constrainType;
    }

    public Double getRightValue() {
        return rightValue;
    }

    public void putConstrainElement(Variable variable, Double value){
        if (value != 0) {
            this.combinationMap.put(variable, value);
        }
    }

    public void setRightValue(Double rightValue) {
        this.rightValue = rightValue;
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<Variable, Double>> iterator = this.combinationMap.entrySet().iterator();
        Map.Entry<Variable, Double> tempElement = iterator.next();
        Variable tempVariable = tempElement.getKey();
        Double tempValue = tempElement.getValue();
        stringBuilder.append(tempValue).append("*").append(tempVariable.getName());
        while (iterator.hasNext()) {
            tempElement = iterator.next();
            tempVariable = tempElement.getKey();
            tempValue = tempElement.getValue();
            if (tempValue > 0) {
                stringBuilder.append("+");
            }
            stringBuilder.append(tempValue).append("*").append(tempVariable.getName());
        }
        switch (this.constrainType) {
            case ConstrainType.LEQ: stringBuilder.append("<="); break;
            case ConstrainType.GEQ: stringBuilder.append(">="); break;
            case ConstrainType.EQ: stringBuilder.append("="); break;
        }
        stringBuilder.append(this.rightValue);
        return stringBuilder.toString();
    }
}
