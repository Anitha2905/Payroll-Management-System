package com.payroll.model;

public class Contractor extends Employee {
    private double dailyRate;
    private int workingDays;

    public Contractor() {
        super();
    }

    public Contractor(int id, String name, String email, int departmentId,
                     String employeeType, String dateOfJoining,
                     double dailyRate, int workingDays) {
        super(id, name, email, departmentId, employeeType, dateOfJoining);
        this.dailyRate = dailyRate;
        this.workingDays = workingDays;
    }

    @Override
    public double calculateSalary() {
        return dailyRate * workingDays;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }
}
