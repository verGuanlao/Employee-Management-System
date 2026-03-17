// src/api/employeeApi.js
import axios from "axios";

// Base URL of your Spring Boot backend
const API_BASE_URL = "http://localhost:8080/api";


// Fetch paginated departments
export async function fetchDepartments(page = 0, size = 10) {
  const response = await axios.get(`${API_BASE_URL}/departments`, {
    params: { page, size },
  });
  return response.data; // ApiResponse<Page<DepartmentDTO>>
}

// Search departments by name
export async function searchDepartments(query, page = 0, size = 10) {
  const response = await axios.get(`${API_BASE_URL}/departments/search`, {
    params: { query, page, size },
  });
  return response.data;
}

// Fetch employees with optional filters
export async function fetchEmployees({ deptId, minAge, maxAge, page = 0, size = 10 }) {
  const response = await axios.get(`${API_BASE_URL}/employees`, {
    params: { deptId, minAge, maxAge, page, size },
  });
  return response.data; // ApiResponse<EmployeeStatsResponse>
}

// Fetch employee by ID
export async function fetchEmployeeById(employeeId) {
  const response = await axios.get(`${API_BASE_URL}/employees/${employeeId}`);
  return response.data; // ApiResponse<EmployeeDTO>
}

// Search employees by name
export async function searchEmployees(query, page = 0, size = 10) {
  const response = await axios.get(`${API_BASE_URL}/employees/search`, {
    params: { query, page, size },
  });
  return response.data;
}

// Add new employee
export async function addEmployee(employeeDTO) {
  const response = await axios.post(`${API_BASE_URL}/employees`, employeeDTO);
  return response.data; // ApiResponse<EmployeeDTO>
}

// Update employee
export async function updateEmployee(employeeDTO) {
  const response = await axios.put(`${API_BASE_URL}/employees`, employeeDTO);
  return response.data;
}

// Delete employee
export async function deleteEmployee(employeeDTO) {
  const response = await axios.delete(`${API_BASE_URL}/employees`, {
    data: employeeDTO,
  });
  return response.data;
}