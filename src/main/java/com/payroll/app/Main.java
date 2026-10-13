package com.payroll.app;

import com.payroll.dao.AttendanceDAO;
import com.payroll.dao.EmployeeDAO;
import com.payroll.exception.InvalidSalaryException;
import com.payroll.model.Employee;
import com.payroll.model.FullTimeEmployee;
import com.payroll.model.Contractor;
import com.payroll.service.PayrollService;
import com.payroll.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private static final PayrollService payrollService = new PayrollService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Payroll Management System");

        while (true) {
            showMenu();
            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1 -> addEmployee();
                    case 2 -> markAttendance();
                    case 3 -> runPayroll();
                    case 4 -> viewReports();
                    case 5 -> {
                        System.out.println("Exiting system. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid menu choice. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (InvalidSalaryException e) {
                System.out.println("Validation error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n--- Payroll Menu ---");
        System.out.println("1. Add Employee");
        System.out.println("2. Mark Attendance");
        System.out.println("3. Run Payroll for a Month");
        System.out.println("4. View Reports");
        System.out.println("5. Exit");
    }

    private static void addEmployee() throws SQLException {
        System.out.println("\nAdd Employee");

        String name = readNonEmptyString("Enter employee name: ");
        String email = readNonEmptyString("Enter employee email: ");
        int departmentId = readInt("Enter department ID: ");
        String employeeType = readEmployeeType();
        String dateOfJoining = readNonEmptyString("Enter date of joining (yyyy-mm-dd): ");

        Employee employee = new Employee() {
            @Override
            public double calculateSalary() {
                return 0;
            }
        };

        employee.setName(name);
        employee.setEmail(email);
        employee.setDepartmentId(departmentId);
        employee.setEmployeeType(employeeType);
        employee.setDateOfJoining(dateOfJoining);

        employeeDAO.addEmployee(employee);
        System.out.println("Employee added successfully!");
    }

    private static void markAttendance() throws SQLException {
        System.out.println("\nMark Attendance");

        int employeeId = readInt("Enter employee ID: ");
        String date = readNonEmptyString("Enter attendance date (yyyy-mm-dd): ");
        String status = readStatus();

        attendanceDAO.markAttendance(employeeId, date, status);
        System.out.println("Attendance marked successfully!");
    }

    private static void runPayroll() throws SQLException, InvalidSalaryException {
        System.out.println("\nRun Payroll");

        int employeeId = readInt("Enter employee ID: ");
        int month = readInt("Enter month (1-12): ");
        int year = readInt("Enter year: ");
        int totalWorkingDays = readInt("Enter total working days in the month: ");
        int paidDays = readInt("Enter paid working days for the employee: ");

        double basicPay = readPositiveDouble("Enter basic pay: ");
        double hra = readNonNegativeDouble("Enter HRA: ");
        double allowances = readNonNegativeDouble("Enter allowances: ");

        double[] payrollResult = payrollService.calculateMonthlyPayroll(
                employeeId, month, year, basicPay, hra, allowances, totalWorkingDays, paidDays);

        System.out.println("Payroll generated successfully!");
        System.out.println("Gross Pay: " + payrollResult[0]);
        System.out.println("Total Deductions: " + payrollResult[1]);
        System.out.println("Net Pay: " + payrollResult[2]);
    }

    private static void viewReports() throws SQLException {
        System.out.println("\nReports");
        System.out.println("1. Department-wise total salary expenditure this month");
        System.out.println("2. Top 5 highest-paid employees");
        System.out.println("3. Employees with more than 2 unpaid leaves this month");
        System.out.println("4. Back to main menu");

        int choice = readInt("Choose report option: ");

        int month = readInt("Enter month: ");
        int year = readInt("Enter year: ");

        switch (choice) {
            case 1 -> {
                List<String> results = payrollService.departmentWiseSalaryExpenditure(month, year);
                printReport(results, "Department-wise total salary expenditure");
            }
            case 2 -> {
                List<String> results = payrollService.topFiveHighestPaidEmployees(month, year);
                printReport(results, "Top 5 highest-paid employees");
            }
            case 3 -> {
                List<String> results = payrollService.employeesWithMoreThanTwoUnpaidLeaves(month, year);
                printReport(results, "Employees with more than 2 unpaid leaves");
            }
            case 4 -> System.out.println("Returning to menu.");
            default -> System.out.println("Invalid report option.");
        }
    }

    private static void printReport(List<String> results, String title) {
        System.out.println("\n" + title);
        if (results.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (String row : results) {
            System.out.println(row);
        }
    }

    private static String readEmployeeType() {
        while (true) {
            String type = readNonEmptyString("Enter employee type (Full-Time / Contractor): ");
            if (type.equalsIgnoreCase("Full-Time") || type.equalsIgnoreCase("Contractor")) {
                return type.equalsIgnoreCase("Full-Time") ? "Full-Time" : "Contractor";
            }
            System.out.println("Invalid employee type. Please enter Full-Time or Contractor.");
        }
    }

    private static String readStatus() {
        while (true) {
            String status = readNonEmptyString("Enter attendance status (Present / Absent / Leave): ");
            if (status.equalsIgnoreCase("Present") || status.equalsIgnoreCase("Absent") || status.equalsIgnoreCase("Leave")) {
                return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
            }
            System.out.println("Invalid status. Please enter Present, Absent, or Leave.");
        }
    }

    private static String readNonEmptyString(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double readPositiveDouble(String message) {
        while (true) {
            System.out.print(message);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be greater than zero.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double readNonNegativeDouble(String message) {
        while (true) {
            System.out.print(message);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Value cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
