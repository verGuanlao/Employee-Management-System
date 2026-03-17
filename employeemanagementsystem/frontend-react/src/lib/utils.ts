import axios from "axios"
import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

// DTOs
export interface EmployeeDTO {
  employeeId: number | null
  name: string
  birthDate: string
  department: string
  salary: number
}

export interface EmployeeStatsResponse {
  employees: {
    content: EmployeeDTO[]
    totalElements: number
    totalPages: number
    size: number
    number: number
  }
  stats: Record<string, any>
}

export interface ApiResponse<T> {
  status: string
  message: string
  data: T
}

export interface DepartmentDTO {
  departmentId: number
  departmentName: string
}

export interface Page<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

export interface ApiResponse<T> {
  status: string
  message: string
  data: T
}

const EMPLOYEE_API_URL = "http://localhost:8080/api/employees"
const DEPARTMENT_API_URL = "http://localhost:8080/api/departments"

// Fetch employees with stats
export async function fetchEmployees(
  params: {
    deptId?: number
    minAge?: number
    maxAge?: number
    page?: number
    size?: number
  } = {}
): Promise<ApiResponse<EmployeeStatsResponse>> {
  const response = await axios.get<ApiResponse<EmployeeStatsResponse>>(
    EMPLOYEE_API_URL,
    {
      params,
    }
  )
  return response.data
}

// Fetch employee by ID
export async function fetchEmployeeById(
  employeeId: number
): Promise<ApiResponse<EmployeeDTO>> {
  const response = await axios.get<ApiResponse<EmployeeDTO>>(
    `${EMPLOYEE_API_URL}/${employeeId}`
  )
  return response.data
}

// Search employees by name
export async function searchEmployees(
  query: string,
  params: { page?: number; size?: number } = {}
): Promise<ApiResponse<EmployeeStatsResponse>> {
  const response = await axios.get<ApiResponse<EmployeeStatsResponse>>(
    `${EMPLOYEE_API_URL}/search`,
    {
      params: { query, ...params },
    }
  )
  return response.data
}

// Add employee
export async function addEmployee(
  employeeDTO: EmployeeDTO
): Promise<ApiResponse<EmployeeDTO>> {
  const response = await axios.post<ApiResponse<EmployeeDTO>>(
    EMPLOYEE_API_URL,
    employeeDTO
  )
  return response.data
}

// Update employee
export async function updateEmployee(
  employeeDTO: EmployeeDTO
): Promise<ApiResponse<EmployeeDTO>> {
  const response = await axios.put<ApiResponse<EmployeeDTO>>(
    EMPLOYEE_API_URL,
    employeeDTO
  )
  return response.data
}

// Delete employee
export async function deleteEmployee(
  employeeDTO: EmployeeDTO
): Promise<ApiResponse<EmployeeDTO>> {
  const response = await axios.delete<ApiResponse<EmployeeDTO>>(
    EMPLOYEE_API_URL,
    { data: employeeDTO }
  )
  return response.data
}

// Fetch departments
export async function fetchDepartments(
  params: { page?: number; size?: number } = {}
): Promise<ApiResponse<Page<DepartmentDTO>>> {
  const response = await axios.get<ApiResponse<Page<DepartmentDTO>>>(
    DEPARTMENT_API_URL,
    {
      params,
    }
  )
  return response.data
}

// Search departments
export async function searchDepartments(
  query: string,
  params: { page?: number; size?: number } = {}
): Promise<ApiResponse<Page<DepartmentDTO>>> {
  const response = await axios.get<ApiResponse<Page<DepartmentDTO>>>(
    `${DEPARTMENT_API_URL}/search`,
    {
      params: { query, ...params },
    }
  )
  return response.data
}
