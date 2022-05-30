package edu.ecnu.dll.cpl;

import edu.ecnu.dll.cpl.expection.CPLException;
import edu.ecnu.dll.tools.collection.ArraysUtils;
import edu.ecnu.dll.tools.collection.SetUtils;
import edu.ecnu.dll.tools.io.print.MyPrint;
import org.apache.commons.math3.linear.MatrixUtils;

import java.util.*;

public class CPlex {

    private List<Variable> variableList = null;
    private List<Constrain> constrainList = null;
    private Goal goal = null;

    private Boolean solveFlag;

//    private Integer realVariableSize = null;

    private Double[][] augmentCoefficientGoalMatrix = null;

    // 有序地记录基变量的索引

    private Integer[] basicVariableArray = null;
    // 记录非基变量的索引

    private Set<Integer> normalVariableSet = null;



    public CPlex() {
        this.variableList = new ArrayList<>();
        this.constrainList = new ArrayList<>();
    }

    /**
     * 返回初步初始化的 增广coefficient matrix（只选出了不等式的入基变量，原始等式部分没有选出入基变量）
     * @param notEqualSize
     */
    private void initFirstCoefficientMatrixAndBasicVariableArray(int notEqualSize) {
        int extraVariableIndex = variableList.size();
        this.basicVariableArray = new Integer[this.constrainList.size()];
//        this.normalVariableSet = new HashSet<>();
        this.augmentCoefficientGoalMatrix = new Double[this.constrainList.size() + 1][this.variableList.size() + notEqualSize + 1];
        ArraysUtils.setTwoDimensionalDoubleArray(this.augmentCoefficientGoalMatrix, 0.0);
        Integer tempConstrainType;
        Constrain tempConstrain;
        Variable tempVariable;
        Double tempValue;
        for (int i = 0; i < this.constrainList.size(); i++) {
            tempConstrain = this.constrainList.get(i);
            // 当不等式为大于等于时，由于添加-x会导致系数化1时右侧再次变为负数。因此需要添加额外正的基变量。
            // 填写系数
            for (Map.Entry<Variable, Double> entry : tempConstrain.getCombinationMap().entrySet()) {
                tempVariable = entry.getKey();
                tempValue = entry.getValue();
                this.augmentCoefficientGoalMatrix[i][tempVariable.getIndex()] = tempValue;
            }
            this.augmentCoefficientGoalMatrix[i][this.augmentCoefficientGoalMatrix[i].length-1] = tempConstrain.getRightValue();

            // 如果是不等式，还要填写新增变量系数
            tempConstrainType = tempConstrain.getConstrainType();

            if (!tempConstrainType.equals(ConstrainType.EQ)) {
                if (tempConstrainType.equals(ConstrainType.LEQ)) {
                    // 小于等于，则新加变量系数为正
                    this.augmentCoefficientGoalMatrix[i][extraVariableIndex] = 1.0;
                } else {
                    // 大于等于，则新加两个变量一个系数为负一个系数为正，正的作为基变量 // todo
                    this.augmentCoefficientGoalMatrix[i][extraVariableIndex] = -1.0;
                    this.augmentCoefficientGoalMatrix[i][++extraVariableIndex] = 1.0;

//                    shrinkMatrixRow(i, -1.0);
                }
                this.basicVariableArray[i] = extraVariableIndex;
                extraVariableIndex ++;
            }

        }
        double flag = 1;
        int cArrayIndex = this.augmentCoefficientGoalMatrix.length - 1;
        if (this.goal.getGoalType().equals(GoalType.MIN)) {
            flag = -1;
        }
        for (Map.Entry<Variable, Double> entry : this.goal.getCombinationMap().entrySet()) {
            tempVariable = entry.getKey();
            tempValue = entry.getValue();
            this.augmentCoefficientGoalMatrix[cArrayIndex][tempVariable.getIndex()] = tempValue * flag;
        }

    }

    private void initSecondEquationBasicVariablesArray() {
        int endIndex = variableList.size() - 1;
        int tempNewInnerVariableIndex;
        double tempNewInnerVariableCoefficient;
        for (int i = 0; i < this.constrainList.size(); i++) {
            if (this.constrainList.get(i).getConstrainType().equals(ConstrainType.EQ)) {
                // 找到当前非0系数变量
                tempNewInnerVariableIndex = ArraysUtils.getFirstElementIndexWithValueMoreThanGivenValue(this.augmentCoefficientGoalMatrix[i], 0, endIndex,  0.0);
                tempNewInnerVariableCoefficient = this.augmentCoefficientGoalMatrix[i][tempNewInnerVariableIndex];
                // 初等变化将改变量化为1
                shrinkMatrixRow(i, tempNewInnerVariableCoefficient);
                changeGoIntoVariableVectorToUnitVector(i, tempNewInnerVariableIndex);
                this.basicVariableArray[i] = tempNewInnerVariableIndex;
            }
        }
        this.normalVariableSet = new HashSet<>();
        SetUtils.addSeriesNatualNumber(this.normalVariableSet, 0, endIndex);
        for (int i = 0; i < this.basicVariableArray.length; i++) {
            this.normalVariableSet.remove(this.basicVariableArray[i]);
        }
    }

    public void init() {
//        this.realVariableSize = variableList.size();
        // 找出不等式加权的个数，如果为小于等于则记为1，大于等于记为2
        int notEqualSize = 0;
        Integer tempConstrainType;
        for (Constrain constrain : this.constrainList) {
            tempConstrainType = constrain.getConstrainType();
            if (tempConstrainType.equals(ConstrainType.LEQ)) {
                ++ notEqualSize;
            } else if (tempConstrainType.equals(ConstrainType.GEQ)) {
                notEqualSize += 2;
            }
        }
        // 初始化增广系数矩阵
        initFirstCoefficientMatrixAndBasicVariableArray(notEqualSize);
        initSecondEquationBasicVariablesArray();

    }

    public Variable addAndReturnVariable() {
        int newIndex = this.variableList.size();
        Variable newVariable = new Variable("x_"+newIndex);
        this.variableList.add(newVariable);
        return newVariable;
    }

    public List<Variable> addAndReturnVariableList(int variableSize) {
        int basicIndex = this.variableList.size();
        List<Variable> newAddedVariableList =  new ArrayList<>();
        Variable newVariable;
        for (int i = 0; i < variableSize; i++, basicIndex++) {
            newVariable = new Variable("x_"+basicIndex);
            this.variableList.add(newVariable);
            newAddedVariableList.add(newVariable);
        }
        return newAddedVariableList;
    }

    public void addConstrain(Constrain constrain) {
        Constrain newConstrain = constrain;
        if (constrain.getRightValue() < 0.0) {
            // 为了保证初始解都不是负数，需要将约束的右侧都化为非负值。
            Variable tempVariable;
            Double tempValue;
            newConstrain = new Constrain();
            for (Map.Entry<Variable, Double> entry : constrain.getCombinationMap().entrySet()) {
                tempVariable = entry.getKey();
                tempValue = - entry.getValue();
                newConstrain.putConstrainElement(tempVariable, tempValue);
            }
            newConstrain.setConstrainType(ConstrainType.reverse(constrain.getConstrainType()));
            newConstrain.setRightValue(-constrain.getRightValue());
        }
        this.constrainList.add(newConstrain);
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    private Integer getOuterBasicVariableIndex(Integer innerBasicIndex) {
        Double[] ratio = new Double[basicVariableArray.length];
        int bArrayIndex =  this.augmentCoefficientGoalMatrix[0].length - 1;
        for (int i = 0; i < ratio.length; i++) {
            ratio[i] = this.augmentCoefficientGoalMatrix[i][bArrayIndex] / this.augmentCoefficientGoalMatrix[i][innerBasicIndex];
        }
        int positionInBasicVariableArray = ArraysUtils.getDoubleMinValueIndexWithMinimalValueConstrain(ratio,0.0);
        return positionInBasicVariableArray;
    }

    private void shrinkMatrixRow(int rowIndex, double shrinkCoefficientValue) {
        int size = this.augmentCoefficientGoalMatrix[0].length;
        for (int j = 0; j < size; j++) {
            this.augmentCoefficientGoalMatrix[rowIndex][j] /= shrinkCoefficientValue;
        }
    }


    private void primaryRowTransform(int basicRowIndex, double amplifyRatio, int toRowIndex) {
        int size = this.augmentCoefficientGoalMatrix[0].length;
        for (int j = 0; j < size; j++) {
            this.augmentCoefficientGoalMatrix[toRowIndex][j] += this.augmentCoefficientGoalMatrix[basicRowIndex][j] * amplifyRatio;
        }
    }

    /**
     * 将其他行的基本行元素所在列全部化为0
     * @param basicRowIndex
     */
    private void changeGoIntoVariableVectorToUnitVector(int basicRowIndex, int basicColIndex) {
        double ratio;
        for (int i = 0; i < this.augmentCoefficientGoalMatrix.length; i++) {
            if (i == basicRowIndex) {
                continue;
            }
            ratio = - this.augmentCoefficientGoalMatrix[i][basicColIndex] / this.augmentCoefficientGoalMatrix[basicRowIndex][basicColIndex];
            this.primaryRowTransform(basicRowIndex, ratio, i);
        }
    }

    /**
     * 单次迭代，如果不可迭代返回false，如果可迭代，迭代后返回true
     * @return
     */
    private boolean solveIterate() throws CPLException {
        // 1. 获取入基变量 (获取目标变量系数中非基变量的最大值对应的变量坐标)
        int cArrayIndex = this.augmentCoefficientGoalMatrix.length - 1;
        Integer goIntoVariableIndex = ArraysUtils.getDoubleMaxValueIndexInGivenIndexSet(this.augmentCoefficientGoalMatrix[cArrayIndex], this.normalVariableSet);
        if (this.augmentCoefficientGoalMatrix[cArrayIndex][goIntoVariableIndex] < 0) {
            return false;
        }
        // 2. 获取出基变量 (rightConst和入基变量系数比值最小的那个)
        int goOutVariableIndexPosition = getOuterBasicVariableIndex(goIntoVariableIndex);
        if (goOutVariableIndexPosition < 0) {
            throw new CPLException("There is no result!");
        }
        Integer goOutVariableIndex = this.basicVariableArray[goOutVariableIndexPosition];
        // 3. 替换出基变量为入基变量
        this.basicVariableArray[goOutVariableIndexPosition] = goIntoVariableIndex;
        this.normalVariableSet.remove(goIntoVariableIndex);
        this.normalVariableSet.add(goOutVariableIndex);

        // 初等变换将新的基变量所在列化为单位向量
        this.shrinkMatrixRow(goOutVariableIndexPosition, this.augmentCoefficientGoalMatrix[goOutVariableIndexPosition][goIntoVariableIndex]);
        this.changeGoIntoVariableVectorToUnitVector(goOutVariableIndexPosition, goIntoVariableIndex);
        return true;

    }

    public void solve() {
        boolean statues;
        try {
            do {
                statues = solveIterate();
            } while (statues);
            solveFlag = true;
        } catch (CPLException e) {
            solveFlag = false;
//            e.printStackTrace();
        }

    }

    public Double getVariableValue(Variable variable) {
        Integer index = variable.getIndex();
        int position = ArraysUtils.findIndexOfObject(this.basicVariableArray, index);
        if (position < 0) {
            return 0.0;
        }
        return this.augmentCoefficientGoalMatrix[index][this.augmentCoefficientGoalMatrix[0].length-1];
    }

    public Map<Variable, Double> getFinalBasicVariableValuePair() {
        Map<Variable, Double> resultMap = new TreeMap<>();
        Integer index;
        Variable variable;
        int bArrayIndex = this.augmentCoefficientGoalMatrix[0].length-1;
        for (int i = 0; i < this.basicVariableArray.length; i++) {
            index = this.basicVariableArray[i];
            if (index < this.variableList.size()) {
                resultMap.put(this.variableList.get(index), this.augmentCoefficientGoalMatrix[i][bArrayIndex]);
            }
        }
        return resultMap;
    }

    public Double getResult() {
        int rowIndex = this.augmentCoefficientGoalMatrix.length - 1;
        int colIndex = this.augmentCoefficientGoalMatrix[0].length - 1;
        return this.augmentCoefficientGoalMatrix[rowIndex][colIndex];
    }



}
