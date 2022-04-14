package org.vafifteen;

public class Employee {
    private final String name;
    private final int id;
    private final String department;
    private final int deptId;
    private final float angle;

    public Employee(String name, int id, String department, int deptId, float angle) {
        this.name = name;
        this.id = id;
        this.department = department;
        this.deptId = deptId;
        this.angle = angle;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public int getDeptId() {
        return deptId;
    }

    public float getAngle() {
        return angle;
    }
}
