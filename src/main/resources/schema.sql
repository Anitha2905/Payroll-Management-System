-- Department table stores all departments in the company.
CREATE TABLE IF NOT EXISTS department (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(100) NOT NULL UNIQUE
);

-- Employee table stores personal and job information for each employee.
CREATE TABLE IF NOT EXISTS employee (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    department_id INT NOT NULL,
    employee_type VARCHAR(20) NOT NULL CHECK (employee_type IN ('Full-Time', 'Contractor')),
    date_of_joining DATE NOT NULL,
    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id) REFERENCES department(department_id)
);

-- Salary table stores the pay structure for each employee.
CREATE TABLE IF NOT EXISTS salary (
    salary_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    basic_pay DECIMAL(10,2) NOT NULL,
    hra DECIMAL(10,2) DEFAULT 0,
    allowances DECIMAL(10,2) DEFAULT 0,
    pf_deduction DECIMAL(10,2) DEFAULT 0,
    tax_deduction DECIMAL(10,2) DEFAULT 0,
    CONSTRAINT fk_salary_employee
        FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
);

-- Attendance table records whether an employee was present, absent, or on leave for a date.
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('Present', 'Absent', 'Leave')),
    CONSTRAINT fk_attendance_employee
        FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
);

-- PayrollRun stores the monthly pay computation result for each employee.
CREATE TABLE IF NOT EXISTS payroll_run (
    payroll_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    month INT NOT NULL CHECK (month BETWEEN 1 AND 12),
    year INT NOT NULL,
    gross_pay DECIMAL(10,2) NOT NULL,
    total_deductions DECIMAL(10,2) NOT NULL,
    net_pay DECIMAL(10,2) NOT NULL,
    generated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payroll_employee
        FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
);
