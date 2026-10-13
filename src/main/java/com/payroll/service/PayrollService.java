package com.payroll.service;

import com.payroll.dao.PayrollDAO;
import com.payroll.dao.SalaryDAO;
import com.payroll.exception.InvalidSalaryException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PayrollService {
    private final SalaryDAO salaryDAO = new SalaryDAO();
    private final PayrollDAO payrollDAO = new PayrollDAO();

    public void validateSalary(double basicPay, double hra, double allowances)
            throws InvalidSalaryException {
        if (basicPay <= 0 || hra < 0 || allowances < 0) {
            throw new InvalidSalaryException("Salary values must be positive or zero. Basic pay must be greater than zero.");
        }
    }

    public double[] calculateMonthlyPayroll(int employeeId, int month, int year, double basicPay,
                                           double hra, double allowances, int totalWorkingDays,
                                           int paidDays) throws SQLException, InvalidSalaryException {
        validateSalary(basicPay, hra, allowances);

        double grossPay;
        double pfDeduction;
        double taxDeduction;

        grossPay = basicPay + hra + allowances;
        pfDeduction = basicPay * 0.12;
        taxDeduction = 0.0;

        if (grossPay > 50000) {
            taxDeduction = grossPay * 0.10;
        }

        double attendanceFactor = (double) paidDays / totalWorkingDays;
        if (attendanceFactor < 0) {
            attendanceFactor = 0;
        }
        if (attendanceFactor > 1) {
            attendanceFactor = 1;
        }

        double adjustedGross = grossPay * attendanceFactor;
        double totalDeductions = (pfDeduction + taxDeduction) * attendanceFactor;
        double netPay = adjustedGross - totalDeductions;

        salaryDAO.addOrUpdateSalary(employeeId, basicPay, hra, allowances, pfDeduction, taxDeduction);
        payrollDAO.savePayrollEntry(employeeId, month, year, adjustedGross, totalDeductions, netPay);

        return new double[]{adjustedGross, totalDeductions, netPay};
    }

    public List<String> departmentWiseSalaryExpenditure(int month, int year) throws SQLException {
        return payrollDAO.getDepartmentWiseSalaryExpenditure(month, year);
    }

    public List<String> topFiveHighestPaidEmployees(int month, int year) throws SQLException {
        return payrollDAO.getTopFiveHighestPaidEmployees(month, year);
    }

    public List<String> employeesWithMoreThanTwoUnpaidLeaves(int month, int year) throws SQLException {
        return payrollDAO.getEmployeesWithMoreThanTwoUnpaidLeaves(month, year);
    }
}
