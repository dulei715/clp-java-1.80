package edu.ecnu.dll.cpl;

import java.util.Objects;

public class Variable implements Comparable<Variable> {
    private String name = null;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        String[] s = this.name.split("_");
        return Integer.parseInt(s[s.length-1]);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Variable v) {
        return this.name.compareTo(v.name);
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                '}';
    }
}
