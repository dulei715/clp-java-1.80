package edu.ecnu.dll.cpl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Constrain {

    private TreeMap<Variable, Double> combinationMap = null;
    private Integer constrainTypeValue = null;
    private Double rightValue = null;

    public Constrain() {
        this.constrainTypeValue = ConstrainType.LEQ;
        this.combinationMap = new TreeMap<>();
        this.rightValue = 0.0;
    }

    public Constrain(TreeMap<Variable, Double> combinationMap, int constrainTypeValue, Double rightValue) {
        this.combinationMap = combinationMap;
        this.constrainTypeValue = constrainTypeValue;
        this.rightValue = rightValue;
    }

    public static Constrain copyToNewConstrain(Constrain oldConstrain) {
        TreeMap<Variable, Double> newCombinationMap = new TreeMap<>();
        for (Map.Entry<Variable, Double> oldEntry : oldConstrain.combinationMap.entrySet()) {
            newCombinationMap.put(oldEntry.getKey(), oldEntry.getValue());
        }
        return new Constrain(newCombinationMap, oldConstrain.constrainTypeValue, oldConstrain.rightValue);
    }

    public void setConstrainTypeValue(int constrainTypeValue) {
        this.constrainTypeValue = constrainTypeValue;
    }

    public TreeMap<Variable, Double> getCombinationMap() {
        return combinationMap;
    }

    public Integer getConstrainTypeValue() {
        return constrainTypeValue;
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

    // 将原有constrain扩大并取代原有constrain
    public void amplifyIn(Double factorValue) {
        this.rightValue  = this.rightValue * factorValue;
        for (Map.Entry<Variable, Double> variableDoubleEntry : this.combinationMap.entrySet()) {
            variableDoubleEntry.setValue(variableDoubleEntry.getValue() * factorValue);
        }

    }

    // 将原有constrain扩大并生成新的constrain，原来的constrain不变
    public Constrain amplifyNew(Double factorValue) {
//        Constrain constrain = new Constrain();
        Integer newConstrainTypeValue = this.constrainTypeValue;
        if (factorValue < 0) {
            newConstrainTypeValue = ConstrainType.reverse(constrainTypeValue);
        }
        Double newRightValue  = this.rightValue * factorValue;
        TreeMap<Variable, Double> newCombinationMap = new TreeMap<>();
        for (Map.Entry<Variable, Double> entry : this.combinationMap.entrySet()) {
            newCombinationMap.put(entry.getKey(), entry.getValue() * factorValue);
        }
        return new Constrain(newCombinationMap, newConstrainTypeValue, newRightValue);
    }

    // 将原有constrain加上当前constrain并取代原有constrain
    public void addIn(Constrain addedConstrain) {
        this.rightValue += addedConstrain.rightValue;
        Variable tempKey;
        Double tempValue, originalValue;
        for (Map.Entry<Variable, Double> entry : addedConstrain.combinationMap.entrySet()) {
            tempKey = entry.getKey();
            tempValue = entry.getValue();

            if (this.combinationMap.containsKey(tempKey)) {
                originalValue = this.combinationMap.get(tempKey);
            } else {
                originalValue = 0.0;
            }
            this.combinationMap.put(tempKey, originalValue + tempValue);
        }
    }



//    /**
//     * 1. 先处理等式，
//     *    (1) 将等式右侧常数项化为非负
//     *    (2) 找出基变量，并且把该基变量对应的其他等式和不等式的系数化为0
//     *
//     * 2. 再处理不等式
//     *    (1) 将不等式右侧常数化为非负
//     *    (2) 根据不等式符号，添加相应的基变量
//     * @param constrainList
//     * @return 等式编号 : 基变量下标
//     */
//    public static Map<Integer, Integer> normalizeConstrainListByEquation(List<Constrain> constrainList) {
//        // 形式化等式，并用等式处理不等式 (等式右侧化为正数，选出基变量，并将基变量系数化为1，将其他等式和不等式对应的该基变量的系数化为0)
//        Constrain tempConstrain;
//        Integer tempConstrainTypeValue;
//        for (int i = 0; i < constrainList.size(); i++) {
//            tempConstrain = constrainList.get(i);
//            // 1. 将右侧非负化
//            if (tempConstrain.getConstrainTypeValue().equals(ConstrainType.EQ)) {
//                for (int j = 0; j < constrainList.size(); j++) {
//
//                }
//            }
//        }
//        // 形式化不等式(右侧常数化为正数)
//        for (int i = 0; i < constrainList.size(); i++) {
//
//        }
//    }





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
        switch (this.constrainTypeValue) {
            case ConstrainType.LEQ: stringBuilder.append("<="); break;
            case ConstrainType.GEQ: stringBuilder.append(">="); break;
            case ConstrainType.EQ: stringBuilder.append("="); break;
        }
        stringBuilder.append(this.rightValue);
        return stringBuilder.toString();
    }
}
