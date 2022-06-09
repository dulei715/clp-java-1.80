package edu.ecnu.dll;

import edu.ecnu.dll.cpl.*;
import edu.ecnu.dll.cpl.expection.CPLException;
import edu.ecnu.dll.tools.io.print.MyPrint;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class CPlexTest {
    @Test
    public void fun1() throws CPLException {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(4);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MIN);
        goal.putGoalElement(variables.get(0), -1.0);
        goal.putGoalElement(variables.get(1), -1.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.EQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(12.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.EQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), 2.0);
        constrain.putConstrainElement(variables.get(3), 1.0);
        constrain.setRightValue(9.0);
        cPlex.addConstrain(constrain);

        cPlex.init();
        cPlex.solve();

        Map<Variable, Double> valuePair = cPlex.getFinalBasicVariableValuePair();
        MyPrint.showMap(valuePair);
        System.out.println(cPlex.getResult());
    }

    @Test
    public void fun2() throws CPLException {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(4);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MIN);
        goal.putGoalElement(variables.get(0), 1.0);
        goal.putGoalElement(variables.get(1), -2.0);
        goal.putGoalElement(variables.get(2), 1.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.EQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(2), -2.0);
        constrain.putConstrainElement(variables.get(3), 1.0);
        constrain.setRightValue(10.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), -1.0);
        constrain.putConstrainElement(variables.get(2), 4.0);
        constrain.setRightValue(8.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), -1.0);
        constrain.putConstrainElement(variables.get(1), 2.0);
        constrain.putConstrainElement(variables.get(2), -4.0);
        constrain.setRightValue(4.0);
        cPlex.addConstrain(constrain);

        cPlex.init();
        cPlex.solve();

        Map<Variable, Double> valuePair = cPlex.getFinalBasicVariableValuePair();
        MyPrint.showMap(valuePair);
        System.out.println(cPlex.getResult());
    }

    @Test
    public void fun3() throws CPLException {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(3);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MIN);
        goal.putGoalElement(variables.get(0), 1.0);
        goal.putGoalElement(variables.get(1), 1.0);
        goal.putGoalElement(variables.get(2), -3.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), -2.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(11.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.GEQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(2), -4.0);
        constrain.setRightValue(3.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.EQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(2), -2.0);
        constrain.setRightValue(1.0);
        cPlex.addConstrain(constrain);

        cPlex.init();
        cPlex.solve();

        Map<Variable, Double> valuePair = cPlex.getFinalBasicVariableValuePair();
        MyPrint.showMap(valuePair);
        System.out.println(cPlex.getResult());
    }

    @Test
    public void fun4() throws CPLException {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(2);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MAX);
        goal.putGoalElement(variables.get(0), 3.0);
        goal.putGoalElement(variables.get(1), 4.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.setRightValue(40.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), 3.0);
        constrain.setRightValue(30.0);
        cPlex.addConstrain(constrain);


        cPlex.init();
        cPlex.solve();

        Map<Variable, Double> valuePair = cPlex.getFinalBasicVariableValuePair();
        MyPrint.showMap(valuePair);
        System.out.println(cPlex.getResult());
    }

    @Test
    public void fun5() throws CPLException {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(2);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MIN);
        goal.putGoalElement(variables.get(0), -3.0);
        goal.putGoalElement(variables.get(1), -4.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.setRightValue(40.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), 3.0);
        constrain.setRightValue(30.0);
        cPlex.addConstrain(constrain);


        cPlex.init();
        cPlex.solve();

        Map<Variable, Double> valuePair = cPlex.getFinalBasicVariableValuePair();
        MyPrint.showMap(valuePair);
        System.out.println(cPlex.getResult());
    }

    @Test
    public void fun6() throws CPLException {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(4);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MAX);
        goal.putGoalElement(variables.get(0), 2.0);
        goal.putGoalElement(variables.get(1), 2.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.EQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(3.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.EQ);
        constrain.putConstrainElement(variables.get(0), -1.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(3), 1.0);
        constrain.setRightValue(1.0);
        cPlex.addConstrain(constrain);


        cPlex.init();
        cPlex.solve();

        Map<Variable, Double> valuePair = cPlex.getFinalBasicVariableValuePair();
        MyPrint.showMap(valuePair);
        System.out.println(cPlex.getResult());
    }

    @Test
    public void fun7() throws CPLException {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(3);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MAX);
        goal.putGoalElement(variables.get(0), 10.0);
        goal.putGoalElement(variables.get(1), 15.0);
        goal.putGoalElement(variables.get(2), 12.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 5.0);
        constrain.putConstrainElement(variables.get(1), 3.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(9.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), -5.0);
        constrain.putConstrainElement(variables.get(1), 6.0);
        constrain.putConstrainElement(variables.get(2), 15.0);
        constrain.setRightValue(15.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.GEQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(5.0);
        cPlex.addConstrain(constrain);

        cPlex.init();
        cPlex.solve();
        if (cPlex.isSolved()) {
            Map<Variable, Double> valuePair = cPlex.getFinalBasicVariableValuePair();
            MyPrint.showMap(valuePair);
            System.out.println(cPlex.getResult());
        } else {
            System.out.println("No result!");
        }

    }

    @Test
    public void fun8() throws CPLException {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(3);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MAX);
        goal.putGoalElement(variables.get(0), 2.0);
        goal.putGoalElement(variables.get(1), 3.0);
        goal.putGoalElement(variables.get(2), -5.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.EQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(7.0);
        cPlex.addConstrain(constrain);


        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.GEQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), -5.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(10.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), 3.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(12.0);
        cPlex.addConstrain(constrain);

        cPlex.init();
        cPlex.solve();
        if (cPlex.isSolved()) {
            Map<Variable, Double> valuePair = cPlex.getFinalBasicVariableValuePair();
            MyPrint.showMap(valuePair);
            System.out.println(cPlex.getResult());
        } else {
            System.out.println("No result!");
        }

    }

    @Test
    public void fun9() throws CPLException {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(2);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MAX);
        goal.putGoalElement(variables.get(0), 8.0);
        goal.putGoalElement(variables.get(1), 6.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.setRightValue(8.0);
        cPlex.addConstrain(constrain);


        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.setRightValue(10.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainTypeValue(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.setRightValue(4.0);
        cPlex.addConstrain(constrain);

        cPlex.init();
        cPlex.solve();
        if (cPlex.isSolved()) {
            Map<Variable, Double> valuePair = cPlex.getFinalBasicVariableValuePair();
            MyPrint.showMap(valuePair);
            System.out.println(cPlex.getResult());
        } else {
            System.out.println("No result!");
        }

    }

}
