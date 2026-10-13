package com.payroll.dao;

import com.payroll.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AttendanceDAO {

    public void markAttendance(int employeeId, String date, String status) throws SQLException {
        String sql = "INSERT INTO attendance (employee_id, date, status) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, status);
            preparedStatement.executeUpdate();
        }
    }
}
