-- ===================
-- DEPARTMENTS
-- ===================
INSERT IGNORE INTO department (department_name) VALUES ('Engineering');
INSERT IGNORE INTO department (department_name) VALUES ('Human Resources');
INSERT IGNORE INTO department (department_name) VALUES ('Finance');
INSERT IGNORE INTO department (department_name) VALUES ('Marketing');
INSERT IGNORE INTO department (department_name) VALUES ('Information Technology');
INSERT IGNORE INTO department (department_name) VALUES ('Operations');
INSERT IGNORE INTO department (department_name) VALUES ('Sales');
INSERT IGNORE INTO department (department_name) VALUES ('Legal');
INSERT IGNORE INTO department (department_name) VALUES ('Research and Development');
INSERT IGNORE INTO department (department_name) VALUES ('Customer Support');

-- ===================
-- EMPLOYEES
-- ===================

-- Engineering
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'James Anderson', '1990-03-15', 85000.00, department_id FROM department WHERE department_name = 'Engineering';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Emily Carter', '1995-07-22', 72000.00, department_id FROM department WHERE department_name = 'Engineering';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Michael Torres', '1988-11-05', 95000.00, department_id FROM department WHERE department_name = 'Engineering';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Sarah Kim', '1998-02-18', 65000.00, department_id FROM department WHERE department_name = 'Engineering';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'David Nguyen', '1985-09-30', 110000.00, department_id FROM department WHERE department_name = 'Engineering';

-- Human Resources
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Lisa Morgan', '1992-04-12', 58000.00, department_id FROM department WHERE department_name = 'Human Resources';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Robert Chen', '1987-08-25', 67000.00, department_id FROM department WHERE department_name = 'Human Resources';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Amanda White', '1996-01-09', 52000.00, department_id FROM department WHERE department_name = 'Human Resources';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Kevin Patel', '1991-06-14', 61000.00, department_id FROM department WHERE department_name = 'Human Resources';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Rachel Brooks', '1983-12-28', 74000.00, department_id FROM department WHERE department_name = 'Human Resources';

-- Finance
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Daniel Scott', '1989-05-17', 92000.00, department_id FROM department WHERE department_name = 'Finance';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Jennifer Lee', '1994-10-03', 78000.00, department_id FROM department WHERE department_name = 'Finance';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Christopher Hall', '1986-07-21', 105000.00, department_id FROM department WHERE department_name = 'Finance';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Michelle Adams', '1997-03-08', 55000.00, department_id FROM department WHERE department_name = 'Finance';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Thomas Wright', '1982-11-19', 118000.00, department_id FROM department WHERE department_name = 'Finance';

-- Marketing
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Jessica Martinez', '1993-08-11', 63000.00, department_id FROM department WHERE department_name = 'Marketing';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Andrew Thompson', '1988-02-27', 71000.00, department_id FROM department WHERE department_name = 'Marketing';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Stephanie Garcia', '1999-06-04', 48000.00, department_id FROM department WHERE department_name = 'Marketing';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Ryan Mitchell', '1985-04-16', 82000.00, department_id FROM department WHERE department_name = 'Marketing';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Lauren Robinson', '1991-09-29', 69000.00, department_id FROM department WHERE department_name = 'Marketing';

-- Information Technology
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Brandon Clark', '1990-01-23', 88000.00, department_id FROM department WHERE department_name = 'Information Technology';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Natalie Lewis', '1995-05-07', 76000.00, department_id FROM department WHERE department_name = 'Information Technology';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Eric Walker', '1987-10-14', 99000.00, department_id FROM department WHERE department_name = 'Information Technology';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Megan Young', '1998-08-31', 62000.00, department_id FROM department WHERE department_name = 'Information Technology';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Patrick Harris', '1984-03-19', 112000.00, department_id FROM department WHERE department_name = 'Information Technology';

-- Operations
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Samantha Allen', '1992-07-06', 57000.00, department_id FROM department WHERE department_name = 'Operations';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Joshua King', '1986-12-22', 73000.00, department_id FROM department WHERE department_name = 'Operations';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Christina Baker', '1996-04-10', 51000.00, department_id FROM department WHERE department_name = 'Operations';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Nathan Nelson', '1989-09-03', 84000.00, department_id FROM department WHERE department_name = 'Operations';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Brittany Carter', '1983-06-15', 96000.00, department_id FROM department WHERE department_name = 'Operations';

-- Sales
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Tyler Perez', '1993-02-28', 59000.00, department_id FROM department WHERE department_name = 'Sales';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Ashley Roberts', '1988-07-17', 68000.00, department_id FROM department WHERE department_name = 'Sales';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Jonathan Turner', '1997-11-24', 46000.00, department_id FROM department WHERE department_name = 'Sales';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Heather Phillips', '1985-05-09', 79000.00, department_id FROM department WHERE department_name = 'Sales';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Gregory Campbell', '1981-01-31', 103000.00, department_id FROM department WHERE department_name = 'Sales';

-- Legal
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Victoria Parker', '1991-10-20', 98000.00, department_id FROM department WHERE department_name = 'Legal';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Benjamin Evans', '1986-04-05', 115000.00, department_id FROM department WHERE department_name = 'Legal';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Amber Edwards', '1994-08-13', 87000.00, department_id FROM department WHERE department_name = 'Legal';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Sean Collins', '1984-02-07', 108000.00, department_id FROM department WHERE department_name = 'Legal';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Diana Stewart', '1979-12-16', 120000.00, department_id FROM department WHERE department_name = 'Legal';

-- Research and Development
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Marcus Sanchez', '1990-06-28', 93000.00, department_id FROM department WHERE department_name = 'Research and Development';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Olivia Morris', '1995-03-14', 81000.00, department_id FROM department WHERE department_name = 'Research and Development';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Derek Rogers', '1987-09-01', 102000.00, department_id FROM department WHERE department_name = 'Research and Development';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Kayla Reed', '1998-05-19', 64000.00, department_id FROM department WHERE department_name = 'Research and Development';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Austin Cook', '1983-08-07', 116000.00, department_id FROM department WHERE department_name = 'Research and Development';

-- Customer Support
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Tiffany Bailey', '1993-11-11', 43000.00, department_id FROM department WHERE department_name = 'Customer Support';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Ian Rivera', '1989-04-24', 48000.00, department_id FROM department WHERE department_name = 'Customer Support';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Courtney Cooper', '1996-09-07', 39000.00, department_id FROM department WHERE department_name = 'Customer Support';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Trevor Richardson', '1987-01-30', 53000.00, department_id FROM department WHERE department_name = 'Customer Support';
INSERT IGNORE INTO employee (name, birth_date, employee_salary, department_id) SELECT 'Vanessa Cox', '1981-07-12', 61000.00, department_id FROM department WHERE department_name = 'Customer Support';