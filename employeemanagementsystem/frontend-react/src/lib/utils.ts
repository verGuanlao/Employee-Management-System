import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';
import api from './axiosConfig';
import { API_URLS } from '@/config/apiConfig';
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

// DTOs
export interface EmployeeDTO {
  employeeId: number | null;
  name: string;
  birthDate: string;
  department: string;
  salary: number;
}

export interface EmployeeStatsResponse {
  employees: {
    content: EmployeeDTO[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
  };
  stats: Record<string, any>;
}

export interface ApiResponse<T> {
  status: string;
  message: string;
  data: T;
}

export interface DepartmentDTO {
  departmentId: number | null;
  departmentName: string | null;
}

export interface UserDTO {
  userId: number | null;
  username: string;
  password: string | null;
  role: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

// Fetch employees with stats
export async function fetchEmployees(
  params: {
    deptId?: number;
    minAge?: number;
    maxAge?: number;
    page?: number;
    size?: number;
  } = {}
): Promise<ApiResponse<EmployeeStatsResponse>> {
  try {
    const response = await api.get<ApiResponse<EmployeeStatsResponse>>(API_URLS.EMPLOYEE, {
      params,
    });
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<EmployeeStatsResponse>;
  }
}

// Fetch employee by ID
export async function fetchEmployeeById(employeeId: number): Promise<ApiResponse<EmployeeDTO>> {
  try {
    const response = await api.get<ApiResponse<EmployeeDTO>>(`${API_URLS.EMPLOYEE}/${employeeId}`);
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<EmployeeDTO>;
  }
}

// Search employees by name
export async function searchEmployees(
  query: string,
  params: { page?: number; size?: number } = {}
): Promise<ApiResponse<EmployeeStatsResponse>> {
  try {
    const response = await api.get<ApiResponse<EmployeeStatsResponse>>(API_URLS.EMPLOYEE_SEARCH, {
      params: { query, ...params },
    });
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<EmployeeStatsResponse>;
  }
}

// Add employee
export async function addEmployee(employeeDTO: EmployeeDTO): Promise<ApiResponse<EmployeeDTO>> {
  try {
    const response = await api.post<ApiResponse<EmployeeDTO>>(API_URLS.EMPLOYEE, employeeDTO);
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<EmployeeDTO>;
  }
}

// Update employee
export async function updateEmployee(employeeDTO: EmployeeDTO): Promise<ApiResponse<EmployeeDTO>> {
  try {
    const response = await api.put<ApiResponse<EmployeeDTO>>(API_URLS.EMPLOYEE, employeeDTO);
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<EmployeeDTO>;
  }
}

// Delete employee
export async function deleteEmployee(employeeDTO: EmployeeDTO): Promise<ApiResponse<EmployeeDTO>> {
  try {
    const response = await api.delete<ApiResponse<EmployeeDTO>>(API_URLS.EMPLOYEE, {
      data: employeeDTO,
    });
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<EmployeeDTO>;
  }
}

// Fetch departments
export async function fetchDepartments(
  params: { page?: number; size?: number } = {}
): Promise<ApiResponse<Page<DepartmentDTO>>> {
  try {
    const response = await api.get<ApiResponse<Page<DepartmentDTO>>>(API_URLS.DEPARTMENT, {
      params,
    });
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<Page<DepartmentDTO>>;
  }
}

// Search departments
export async function searchDepartments(
  query: string,
  params: { page?: number; size?: number } = {}
): Promise<ApiResponse<Page<DepartmentDTO>>> {
  try {
    const response = await api.get<ApiResponse<Page<DepartmentDTO>>>(API_URLS.DEPARTMENT_SEARCH, {
      params: { query, ...params },
    });
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<Page<DepartmentDTO>>;
  }
}

// Add Department
export async function addDepartment(
  departmentDTO: DepartmentDTO
): Promise<ApiResponse<DepartmentDTO>> {
  try {
    const response = await api.post<ApiResponse<DepartmentDTO>>(API_URLS.DEPARTMENT, departmentDTO);
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<DepartmentDTO>;
  }
}

// Update Department
export async function updateDepartment(
  departmentDTO: DepartmentDTO
): Promise<ApiResponse<DepartmentDTO>> {
  try {
    const response = await api.put<ApiResponse<DepartmentDTO>>(API_URLS.DEPARTMENT, departmentDTO);
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<DepartmentDTO>;
  }
}

// Delete Department
export async function deleteDepartment(
  departmentDTO: DepartmentDTO
): Promise<ApiResponse<DepartmentDTO>> {
  try {
    const response = await api.delete<ApiResponse<DepartmentDTO>>(API_URLS.DEPARTMENT, {
      data: departmentDTO,
    });
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<DepartmentDTO>;
  }
}

// Fetch the users depending if there is a search
export async function getAllUsers(params: {
  username?: string;
  page: number;
  size: number;
}): Promise<ApiResponse<Page<UserDTO>>> {
  try {
    const response = await api.get<ApiResponse<Page<UserDTO>>>(API_URLS.USER, { params });
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<Page<UserDTO>>;
  }
}

// create new user
export async function registerUser(userDTO: UserDTO): Promise<ApiResponse<UserDTO>> {
  try {
    const response = await api.post<ApiResponse<UserDTO>>(API_URLS.AUTH_REGISTER, userDTO);
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<UserDTO>;
  }
}

// login user
export async function login(username: string, password: string): Promise<ApiResponse<UserDTO>> {
  try {
    const response = await api.post<ApiResponse<UserDTO>>(API_URLS.AUTH_LOGIN, {
      username,
      password,
    });
    const token = response.headers['authorization']?.replace('Bearer ', '');
    if (token) localStorage.setItem('accessToken', token);
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<UserDTO>;
  }
}

// Update existing user
export async function updateUser(userDTO: UserDTO): Promise<ApiResponse<UserDTO>> {
  try {
    const response = await api.patch<ApiResponse<UserDTO>>(API_URLS.USER, userDTO);
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<UserDTO>;
  }
}

// Delete existing user
export async function deleteUser(userDTO: UserDTO): Promise<ApiResponse<UserDTO>> {
  try {
    const response = await api.delete<ApiResponse<UserDTO>>(API_URLS.USER, { data: userDTO });
    return response.data;
  } catch (error: any) {
    return error.response?.data as ApiResponse<UserDTO>;
  }
}
