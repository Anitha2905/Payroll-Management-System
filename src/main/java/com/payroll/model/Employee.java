package com.payroll.model;

public abstract class Employee {
    private int id;
    private String name;
    private String email;
    private int departmentId;
    private String employeeType;
    private String dateOfJoining;

    public Employee() {
    }

    public Employee(int id, String name, String email, int departmentId,
                    String employeeType, String dateOfJoining) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.departmentId = departmentId;
        this.employeeType = employeeType;
        this.dateOfJoining = dateOfJoining;
    }

    public abstract double calculateSalary();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }
}
