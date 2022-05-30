package edu.ecnu.dll;

import edu.ecnu.dll.cpl.*;
import edu.ecnu.dll.tools.io.print.MyPrint;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class CPlexTest {
    @Test
    public void fun1() {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(4);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MIN);
        goal.putGoalElement(variables.get(0), -1.0);
        goal.putGoalElement(variables.get(1), -1.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainType(ConstrainType.EQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(12.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainType(ConstrainType.EQ);
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
    public void fun2() {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(4);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MIN);
        goal.putGoalElement(variables.get(0), 1.0);
        goal.putGoalElement(variables.get(1), -2.0);
        goal.putGoalElement(variables.get(2), 1.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainType(ConstrainType.EQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(2), -2.0);
        constrain.putConstrainElement(variables.get(3), 1.0);
        constrain.setRightValue(10.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainType(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), -1.0);
        constrain.putConstrainElement(variables.get(2), 4.0);
        constrain.setRightValue(8.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainType(ConstrainType.LEQ);
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
    public void fun3() {
        CPlex cPlex = new CPlex();
        List<Variable> variables = cPlex.addAndReturnVariableList(3);

        Goal goal = new Goal();
        goal.setGoalType(GoalType.MIN);
        goal.putGoalElement(variables.get(0), 1.0);
        goal.putGoalElement(variables.get(1), 1.0);
        goal.putGoalElement(variables.get(2), -3.0);
        cPlex.setGoal(goal);

        Constrain constrain = new Constrain();
        constrain.setConstrainType(ConstrainType.LEQ);
        constrain.putConstrainElement(variables.get(0), 1.0);
        constrain.putConstrainElement(variables.get(1), -2.0);
        constrain.putConstrainElement(variables.get(2), 1.0);
        constrain.setRightValue(11.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainType(ConstrainType.GEQ);
        constrain.putConstrainElement(variables.get(0), 2.0);
        constrain.putConstrainElement(variables.get(1), 1.0);
        constrain.putConstrainElement(variables.get(2), -4.0);
        constrain.setRightValue(3.0);
        cPlex.addConstrain(constrain);

        constrain = new Constrain();
        constrain.setConstrainType(ConstrainType.EQ);
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
}
