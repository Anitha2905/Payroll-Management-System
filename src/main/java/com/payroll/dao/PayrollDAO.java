package com.payroll.dao;

import com.payroll.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    public void savePayrollEntry(int employeeId, int month, int year,
                                double grossPay, double totalDeductions, double netPay) throws SQLException {
        String sql = "INSERT INTO payroll_run (employee_id, month, year, gross_pay, total_deductions, net_pay) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, year);
            preparedStatement.setDouble(4, grossPay);
            preparedStatement.setDouble(5, totalDeductions);
            preparedStatement.setDouble(6, netPay);
            preparedStatement.executeUpdate();
        }
    }

    public List<String> getDepartmentWiseSalaryExpenditure(int month, int year) throws SQLException {
        List<String> report = new ArrayList<>();
        String sql = "SELECT d.department_name, SUM(pr.net_pay) AS total_salary " +
                "FROM payroll_run pr JOIN employee e ON pr.employee_id = e.employee_id " +
                "JOIN department d ON e.department_id = d.department_id " +
                "WHERE pr.month = ? AND pr.year = ? " +
                "GROUP BY d.department_name ORDER BY total_salary DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    report.add(resultSet.getString("department_name") + " : " + resultSet.getDouble("total_salary"));
                }
            }
        }
        return report;
    }

    public List<String> getTopFiveHighestPaidEmployees(int month, int year) throws SQLException {
        List<String> report = new ArrayList<>();
        String sql = "SELECT e.name, pr.net_pay FROM payroll_run pr " +
                "JOIN employee e ON pr.employee_id = e.employee_id " +
                "WHERE pr.month = ? AND pr.year = ? " +
                "ORDER BY pr.net_pay DESC LIMIT 5";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    report.add(resultSet.getString("name") + " : " + resultSet.getDouble("net_pay"));
                }
            }
        }
        return report;
    }

    public List<String> getEmployeesWithMoreThanTwoUnpaidLeaves(int month, int year) throws SQLException {
        List<String> report = new ArrayList<>();
        String sql = "SELECT e.name, COUNT(a.attendance_id) AS unpaid_leave_count " +
                "FROM employee e JOIN attendance a ON e.employee_id = a.employee_id " +
                "WHERE a.status = 'Leave' AND MONTH(a.date) = ? AND YEAR(a.date) = ? " +
                "GROUP BY e.employee_id, e.name HAVING COUNT(a.attendance_id) > 2";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    report.add(resultSet.getString("name") + " : " + resultSet.getInt("unpaid_leave_count"));
                }
            }
        }
        return report;
    }
}
