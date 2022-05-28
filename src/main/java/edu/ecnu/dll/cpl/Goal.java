package edu.ecnu.dll.cpl;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Goal {

    private TreeMap<Variable, Double> combinationMap = null;
    private Integer goalType = null;

    public Goal() {
        this.goalType = GoalType.MAX;
        this.combinationMap = new TreeMap<>();
    }

    public Goal(TreeMap<Variable, Double> combinationMap, Integer goalType) {
        this.combinationMap = combinationMap;
        this.goalType = goalType;
    }

    public void setGoalType(Integer goalType) {
        this.goalType = goalType;
    }

    public void putGoalElement(Variable variable, Double value){
        if (value != 0) {
            this.combinationMap.put(variable, value);
        }
    }

    public TreeMap<Variable, Double> getCombinationMap() {
        return combinationMap;
    }

    public Integer getGoalType() {
        return goalType;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        switch (this.goalType) {
            case GoalType.MAX: stringBuilder.append("Max "); break;
            case GoalType.MIN: stringBuilder.append("Min ");break;
        }
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
        return stringBuilder.toString();
    }
}
