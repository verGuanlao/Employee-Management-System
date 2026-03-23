const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

export const API_URLS = {
  EMPLOYEE: `${API_BASE_URL}/api/employees`,
  EMPLOYEE_SEARCH: `${API_BASE_URL}/api/employees/search`,
  DEPARTMENT: `${API_BASE_URL}/api/departments`,
  DEPARTMENT_SEARCH: `${API_BASE_URL}/api/departments/search`,
  USER: `${API_BASE_URL}/api/users`,
  AUTH: `${API_BASE_URL}/api/auth`,
  AUTH_LOGIN: `${API_BASE_URL}/api/auth/login`,
  AUTH_REGISTER: `${API_BASE_URL}/api/auth/register`,
};
