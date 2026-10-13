package com.payroll.model;

public class FullTimeEmployee extends Employee {
    private double basicPay;
    private double hra;
    private double allowances;

    public FullTimeEmployee() {
        super();
    }

    public FullTimeEmployee(int id, String name, String email, int departmentId,
                           String employeeType, String dateOfJoining,
                           double basicPay, double hra, double allowances) {
        super(id, name, email, departmentId, employeeType, dateOfJoining);
        this.basicPay = basicPay;
        this.hra = hra;
        this.allowances = allowances;
    }

    @Override
    public double calculateSalary() {
        return basicPay + hra + allowances;
    }

    public double getBasicPay() {
        return basicPay;
    }

    public void setBasicPay(double basicPay) {
        this.basicPay = basicPay;
    }

    public double getHra() {
        return hra;
    }

    public void setHra(double hra) {
        this.hra = hra;
    }

    public double getAllowances() {
        return allowances;
    }

    public void setAllowances(double allowances) {
        this.allowances = allowances;
    }
}
