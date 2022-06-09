package edu.ecnu.dll.cpl;

import edu.ecnu.dll.cpl.expection.CPLException;
import edu.ecnu.dll.cpl.struct.BasicVariableEntry;
import edu.ecnu.dll.cpl.struct.ExtendDouble;
import edu.ecnu.dll.tools.collection.ArraysUtils;
import edu.ecnu.dll.tools.collection.SetUtils;

import java.util.*;
@Deprecated
public class CPlexBefore {

    private List<Variable> variableList = null;
    private List<Constrain> constrainList = null;
    // 保证这里的 goal 被形式化为 MAX 形式
    private Goal goal = null;
    private Map.Entry<Integer, ExtendDouble>[] goalVariableEntryArray = null;

    private Boolean solveFlag;

//    private Integer realVariableSize = null;

    private Double[][] augmentCoefficientGoalMatrix = null;
    private ExtendDouble[] judgementNumberArray = null;

    // 有序地记录基变量的索引
//    private Integer[] basicVariableArray = null;
//    private ExtendDouble[] basicVariableParameterArray = null;
    private Map.Entry<Integer, ExtendDouble>[] basicVariableArray = null;

    // 记录非基变量的索引
    private Set<Map.Entry<Integer, ExtendDouble>> normalVariableSet = null;



    public CPlexBefore() {
        this.variableList = new ArrayList<>();
        this.constrainList = new ArrayList<>();
    }

    /**
     * 返回初步初始化的 增广coefficient matrix（只选出了不等式的入基变量，原始等式部分没有选出入基变量）
     * @param notEqualSize
     */
    private void initFirstCoefficientMatrixAndBasicVariableArray(int notEqualSize) {
        int extraVariableIndex = variableList.size();
        this.basicVariableArray = new BasicVariableEntry[this.constrainList.size()];

//        this.augmentCoefficientGoalMatrix = new Double[this.constrainList.size() + 1][this.variableList.size() + notEqualSize + 1];
        // 这里假设为增广矩阵 todo:xxx
        int lineNum = this.variableList.size() + notEqualSize + 1;
        this.augmentCoefficientGoalMatrix = new Double[this.constrainList.size()][lineNum];
        this.judgementNumberArray = new ExtendDouble[lineNum];
        // 这里的goalParameterArray多了一个元素，初始化为0
        this.goalVariableEntryArray = new BasicVariableEntry[lineNum];

        // 初始化
        ArraysUtils.setTwoDimensionalDoubleArray(this.augmentCoefficientGoalMatrix, 0.0);
        ExtendDouble.initializeExtendDoubleArray(this.judgementNumberArray);
        BasicVariableEntry.initializeExtendDoubleArray(this.goalVariableEntryArray);

        Integer tempConstrainType;
        Constrain tempConstrain;
        Variable tempVariable;
        Double tempValue;


        // todo: 1. 先处理等式，
        //          (1) 将等式右侧常数项化为非负
        //          (2) 找出基变量，并且把该基变量对应的其他等式和不等式的系数化为0

        // todo: 2. 再处理不等式
        //          (1) 将不等式右侧常数化为非负
        //          (2) 根据不等式符号，添加相应的基变量

        ExtendDouble tempExtendDouble;
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
            tempConstrainType = tempConstrain.getConstrainTypeValue();

            if (!tempConstrainType.equals(ConstrainType.EQ)) {
                // 不是等式，表示一定有一个新加变量，可以作为初始基变量，该变量要么系数为0要么为-M
                if (tempConstrainType.equals(ConstrainType.LEQ)) {
                    // 小于等于，则新加变量系数为正
                    this.augmentCoefficientGoalMatrix[i][extraVariableIndex] = 1.0;
//                    tempExtendDouble = ExtendDouble.valueOf(0.0);
                } else {
                    // 大于等于，则新加两个变量一个系数为负一个系数为正，正的作为基变量 // todo
                    this.augmentCoefficientGoalMatrix[i][extraVariableIndex] = -1.0;
                    this.augmentCoefficientGoalMatrix[i][++extraVariableIndex] = 1.0;
                    tempExtendDouble = new ExtendDouble(-1.0, 0.0);
//                  // 提前将新加变量需要系数取值为负无穷大的设置好
//                    this.goalVariableEntryArray[extraVariableIndex] = new ExtendDouble(-1.0, 0.0);
                    this.goalVariableEntryArray[extraVariableIndex].setValue(tempExtendDouble);

                }
//                this.basicVariableArray[i] = new BasicVariableEntry(extraVariableIndex, tempExtendDouble);
                this.basicVariableArray[i] = this.goalVariableEntryArray[extraVariableIndex];
                extraVariableIndex ++;
            }

        }

        // 设置其他的goalParameterArray元素
        for (Map.Entry<Variable, Double> entry : this.goal.getCombinationMap().entrySet()) {
            tempVariable = entry.getKey();
            tempValue = entry.getValue();
//            this.goalVariableEntryArray[tempVariable.getIndex()] = ExtendDouble.valueOf(tempValue);
            this.goalVariableEntryArray[tempVariable.getIndex()].setValue(ExtendDouble.valueOf(tempValue));
        }


    }

    private void initSecondEquationBasicVariablesArray() throws CPLException {
        int endIndex = variableList.size() - 1;
        Integer tempNewInnerVariableIndex;
        int beginIndex = 0;
        double tempNewInnerVariableCoefficient;
        for (int i = 0; i < this.constrainList.size(); i++) {
            if (this.constrainList.get(i).getConstrainTypeValue().equals(ConstrainType.EQ)) {
                // 找到当前正系数变量，且该变量不能已经是其他约束表达式的基变量 todo
                do {
                    tempNewInnerVariableIndex = ArraysUtils.getFirstElementIndexWithValueMoreThanGivenValue(this.augmentCoefficientGoalMatrix[i], beginIndex, endIndex,  0.0);
                    beginIndex = tempNewInnerVariableIndex + 1;
                } while (BasicVariableEntry.containKey(this.basicVariableArray, tempNewInnerVariableIndex));
                if (tempNewInnerVariableIndex < 0) {
                    // 说明该等式矛盾，原线性规划无解
                    throw new CPLException("Conflict for Equation " + String.valueOf(i));
                }
                tempNewInnerVariableCoefficient = this.augmentCoefficientGoalMatrix[i][tempNewInnerVariableIndex];
                // 初等变化将改变量化为1
                shrinkMatrixRow(i, tempNewInnerVariableCoefficient);
                changeGoIntoVariableVectorToUnitVector(i, tempNewInnerVariableIndex);
//                this.basicVariableArray[i] = tempNewInnerVariableIndex;
//                this.basicVariableArray[i] = new BasicVariableEntry(tempNewInnerVariableIndex, this.goalVariableEntryArray[tempNewInnerVariableIndex]);
                this.basicVariableArray[i] = this.goalVariableEntryArray[tempNewInnerVariableIndex];
            }
        }
        this.normalVariableSet = new HashSet<>();
        SetUtils.addSeriesIndexOfGivenData(this.normalVariableSet, this.goalVariableEntryArray, 0, endIndex);
        for (int i = 0; i < this.basicVariableArray.length; i++) {
            this.normalVariableSet.remove(this.basicVariableArray[i]);
        }


        // 初始化检验数
        setAllJudgementValue();

    }

    private void setJudgmentValue(Integer variableIndex) {
        Map.Entry<Integer, ExtendDouble> tempBasicVariableEntry;
        ExtendDouble result = this.goalVariableEntryArray[variableIndex].getValue();
        ExtendDouble tempValueA;
        for (int i = 0; i < this.basicVariableArray.length; i++) {
            tempBasicVariableEntry = this.basicVariableArray[i];
            tempValueA = tempBasicVariableEntry.getValue().multiple(this.augmentCoefficientGoalMatrix[i][variableIndex]).negative();
            result = result.add(tempValueA);
        }
        this.judgementNumberArray[variableIndex] = result;
    }

    private void setAllJudgementValue() {
        for (int i = 0; i < this.judgementNumberArray.length; i++) {
            setJudgmentValue(i);
        }
    }

    public void init() throws CPLException {
//        this.realVariableSize = variableList.size();
        // 找出不等式加权的个数，如果为小于等于则记为1，大于等于记为2
        int notEqualSize = 0;
        Integer tempConstrainType;
        for (Constrain constrain : this.constrainList) {
            tempConstrainType = constrain.getConstrainTypeValue();
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

    // todo: 化简
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
            newConstrain.setConstrainTypeValue(ConstrainType.reverse(constrain.getConstrainTypeValue()));
            newConstrain.setRightValue(-constrain.getRightValue());
        }
        this.constrainList.add(newConstrain);
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
        this.goal.normalizeToMaxEqualGoal();
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
//        int cArrayIndex = this.augmentCoefficientGoalMatrix.length - 1;
//        Integer goIntoVariableIndex = ArraysUtils.getDoubleMaxValueIndexInGivenIndexSet(this.augmentCoefficientGoalMatrix[cArrayIndex], this.normalVariableSet);
        // 最后一个是记录的是优化的结果，不是检验数
        int endIndex = this.judgementNumberArray.length - 2;
        Integer goIntoVariableIndex = ExtendDouble.getMaxValueIndexInGivenEntrySetWithLimitEndIndex(this.judgementNumberArray, this.goalVariableEntryArray, this.normalVariableSet, endIndex);
//        Map.Entry<Integer, ExtendDouble> goIntoVariableEntry = new BasicVariableEntry(goIntoVariableIndex, this.goalVariableEntryArray[goIntoVariableIndex]);
        Map.Entry<Integer, ExtendDouble> goIntoVariableEntry = this.goalVariableEntryArray[goIntoVariableIndex];
        if (this.judgementNumberArray[goIntoVariableIndex].compareTo(0.0) <= 0) {
            return false;
        }
        // 2. 获取出基变量 (rightConst和入基变量系数比值最小的那个)
        int goOutVariableIndexPosition = getOuterBasicVariableIndex(goIntoVariableIndex);
        if (goOutVariableIndexPosition < 0) {
            throw new CPLException("There is no result!");
        }
        Map.Entry<Integer, ExtendDouble> goOutVariableEntry = this.basicVariableArray[goOutVariableIndexPosition];
        // 3. 替换出基变量为入基变量
        this.basicVariableArray[goOutVariableIndexPosition] = goIntoVariableEntry;
        this.normalVariableSet.remove(goIntoVariableEntry);
        this.normalVariableSet.add(goOutVariableEntry);

        // 初等变换将新的基变量所在列化为单位向量，并更新检验数
        this.shrinkMatrixRow(goOutVariableIndexPosition, this.augmentCoefficientGoalMatrix[goOutVariableIndexPosition][goIntoVariableIndex]);
        this.changeGoIntoVariableVectorToUnitVector(goOutVariableIndexPosition, goIntoVariableIndex);
        setAllJudgementValue();
        return true;

    }

    public void solve() {
        boolean statues;
        try {
            do {
                statues = solveIterate();
            } while (statues);
            if (Math.abs(this.judgementNumberArray[this.judgementNumberArray.length-1].coefficientOfM) < Constant.DEFAULT_PRECISION) {
                solveFlag = true;
            } else {
                solveFlag = false;
            }
        } catch (CPLException e) {
            solveFlag = false;
//            e.printStackTrace();
        }

    }

    public Double getVariableValue(Variable variable) {
        Integer index = variable.getIndex();
        int position = ArraysUtils.findIndexOfGivenKey(this.basicVariableArray, index);
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
            index = this.basicVariableArray[i].getKey();
            if (index < this.variableList.size()) {
                resultMap.put(this.variableList.get(index), this.augmentCoefficientGoalMatrix[i][bArrayIndex]);
            }
        }
        return resultMap;
    }

    public boolean isSolved() {
        return this.solveFlag;
    }

    public Double getResult() {
//        int rowIndex = this.augmentCoefficientGoalMatrix.length - 1;
//        int colIndex = this.augmentCoefficientGoalMatrix[0].length - 1;
//        return this.augmentCoefficientGoalMatrix[rowIndex][colIndex];
        ExtendDouble result = this.judgementNumberArray[this.judgementNumberArray.length-1].negative();
        if (result.isInfinity()) {
            return Double.MAX_VALUE;
        }
        return result.getConstant();
    }



}
