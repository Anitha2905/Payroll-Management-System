package com.payroll.dao;

import com.payroll.model.Employee;
import com.payroll.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public void addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee (name, email, department_id, employee_type, date_of_joining) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            preparedStatement.setInt(3, employee.getDepartmentId());
            preparedStatement.setString(4, employee.getEmployeeType());
            preparedStatement.setString(5, employee.getDateOfJoining());

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    employee.setId(resultSet.getInt(1));
                }
            }
        }
    }

    public Employee getEmployeeById(int employeeId) throws SQLException {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employeeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Employee employee = new Employee() {
                        @Override
                        public double calculateSalary() {
                            return 0;
                        }
                    };

                    employee.setId(resultSet.getInt("employee_id"));
                    employee.setName(resultSet.getString("name"));
                    employee.setEmail(resultSet.getString("email"));
                    employee.setDepartmentId(resultSet.getInt("department_id"));
                    employee.setEmployeeType(resultSet.getString("employee_type"));
                    employee.setDateOfJoining(resultSet.getString("date_of_joining"));
                    return employee;
                }
            }
        }

        return null;
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee ORDER BY employee_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee() {
                    @Override
                    public double calculateSalary() {
                        return 0;
                    }
                };

                employee.setId(resultSet.getInt("employee_id"));
                employee.setName(resultSet.getString("name"));
                employee.setEmail(resultSet.getString("email"));
                employee.setDepartmentId(resultSet.getInt("department_id"));
                employee.setEmployeeType(resultSet.getString("employee_type"));
                employee.setDateOfJoining(resultSet.getString("date_of_joining"));
                employees.add(employee);
            }
        }

        return employees;
    }
}
