package com.payroll.dao;

import com.payroll.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalaryDAO {

    public void addOrUpdateSalary(int employeeId, double basicPay, double hra,
                                 double allowances, double pfDeduction, double taxDeduction) throws SQLException {
        String sql = "INSERT INTO salary (employee_id, basic_pay, hra, allowances, pf_deduction, tax_deduction) " +
                "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                "basic_pay = VALUES(basic_pay), hra = VALUES(hra), allowances = VALUES(allowances), " +
                "pf_deduction = VALUES(pf_deduction), tax_deduction = VALUES(tax_deduction)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setDouble(2, basicPay);
            preparedStatement.setDouble(3, hra);
            preparedStatement.setDouble(4, allowances);
            preparedStatement.setDouble(5, pfDeduction);
            preparedStatement.setDouble(6, taxDeduction);
            preparedStatement.executeUpdate();
        }
    }

    public boolean salaryExistsForEmployee(int employeeId) throws SQLException {
        String sql = "SELECT 1 FROM salary WHERE employee_id = ? LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employeeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
