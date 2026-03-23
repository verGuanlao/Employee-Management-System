import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import api from './axiosConfig';
import { API_URLS } from '@/config/apiConfig';
import { toast } from 'sonner';
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

export const exportEmployeesToPDF = async (filters: {
  department?: string;
  departmentId?: number | null;
  minAge?: number | null;
  maxAge?: number | null;
}) => {
  const params: any = { page: 0, size: 9999 };
  if (filters.departmentId) params.deptId = filters.departmentId;
  if (filters.minAge != null) params.minAge = filters.minAge;
  if (filters.maxAge != null) params.maxAge = filters.maxAge;

  const res: ApiResponse<EmployeeStatsResponse> = await fetchEmployees(params);

  if (res.status !== 'success') {
    toast.error(res.message ?? 'Failed to export employees');
    return;
  }

  const employees = res.data.employees.content;
  const doc = new jsPDF();
  const pageWidth = doc.internal.pageSize.getWidth();
  const pageHeight = doc.internal.pageSize.getHeight();
  const PRIMARY: [number, number, number] = [44, 53, 57];

  // header background bar
  doc.setFillColor(PRIMARY[0], PRIMARY[1], PRIMARY[2]);
  doc.rect(0, 0, pageWidth, 24, 'F');

  // main title
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(20);
  doc.setTextColor(255, 255, 255);
  doc.text('EMPLOYEE REPORT', pageWidth / 2, 13, { align: 'center' });

  // generated date
  doc.setFontSize(7);
  doc.setTextColor(180, 180, 180);
  doc.text(
    `Generated: ${new Date().toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    })}`,
    pageWidth - 14,
    23,
    { align: 'right' }
  );

  let currentY = 32;

  // filters
  const activeFilters: string[] = [];
  if (filters.department) activeFilters.push(`Department: ${filters.department}`);
  if (filters.minAge != null) activeFilters.push(`Min Age: ${filters.minAge}`);
  if (filters.maxAge != null) activeFilters.push(`Max Age: ${filters.maxAge}`);

  if (activeFilters.length > 0) {
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(8);
    doc.setTextColor(80, 80, 80);
    doc.text('ACTIVE FILTERS', 14, currentY);
    currentY += 4;

    doc.setFont('helvetica', 'normal');
    doc.setFontSize(8);
    doc.setTextColor(100, 100, 100);
    doc.text(activeFilters.join('   |   '), 14, currentY);
    currentY += 8;
  }

  // summary label
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(9);
  doc.setTextColor(60, 60, 60);
  doc.text('SUMMARY', 14, currentY);
  currentY += 2;

  // divider
  doc.setDrawColor(200, 200, 200);
  doc.setLineWidth(0.3);
  doc.line(14, currentY, pageWidth - 14, currentY);
  currentY += 4;

  // stats table
  autoTable(doc, {
    startY: currentY,
    head: [['Employee Count', 'Average Salary', 'Average Age']],
    body: [
      [
        res.data.employees.totalElements,
        `Php ${res.data.stats.averageSalary?.toLocaleString('en-US', { minimumFractionDigits: 2 }) ?? '0.00'}`,
        res.data.stats.averageAge?.toFixed(1) ?? '0.0',
      ],
    ],
    styles: {
      fontSize: 9,
      font: 'helvetica',
    },
    headStyles: {
      fillColor: PRIMARY,
      textColor: 255,
      fontStyle: 'bold',
      fontSize: 8,
      halign: 'center',
      cellPadding: 4,
    },
    bodyStyles: {
      halign: 'center',
      fontSize: 11,
      fontStyle: 'bold',
      textColor: [33, 33, 33],
      cellPadding: 5,
    },
    columnStyles: {
      0: { cellWidth: 50 },
      1: { cellWidth: 80 },
      2: { cellWidth: 50 },
    },
    tableWidth: 180,
    margin: { left: 14 },
  });

  currentY = (doc as any).lastAutoTable.finalY + 12;

  // employee list
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(9);
  doc.setTextColor(60, 60, 60);
  doc.text('EMPLOYEE LIST', 14, currentY);
  currentY += 2;

  // divider
  doc.setDrawColor(200, 200, 200);
  doc.setLineWidth(0.3);
  doc.line(14, currentY, pageWidth - 14, currentY);
  currentY += 4;

  // employee table
  autoTable(doc, {
    startY: currentY,
    showHead: 'everyPage',
    head: [['ID', 'Name', 'Department', 'Birthdate', 'Age', 'Salary']],
    body: employees.map((emp) => [
      emp.employeeId,
      emp.name,
      emp.department,
      emp.birthDate ?? '-',
      calculateAge(emp.birthDate) ?? '-',
      `Php ${emp.salary.toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      })}`,
    ]),
    styles: {
      fontSize: 8,
      font: 'helvetica',
      cellPadding: 3,
    },
    headStyles: {
      fillColor: PRIMARY,
      textColor: 255,
      fontStyle: 'bold',
      fontSize: 8,
      halign: 'center',
      cellPadding: 4,
    },
    bodyStyles: {
      textColor: [33, 33, 33],
      fontSize: 8,
    },
    alternateRowStyles: {
      fillColor: [245, 245, 245],
    },
    columnStyles: {
      0: { cellWidth: 15, halign: 'center' }, // ID
      1: { cellWidth: 45, halign: 'center' }, // Name
      2: { cellWidth: 35, halign: 'center' }, // Department
      3: { cellWidth: 28, halign: 'center' }, // Birthdate
      4: { cellWidth: 15, halign: 'center' }, // Age
      5: { cellWidth: 35, halign: 'right' }, // Salary
    },
    tableWidth: 173,
    margin: { left: 14, right: 14 },

    // footer with page numbers on every page
    didDrawPage: (data) => {
      // footer line
      doc.setDrawColor(200, 200, 200);
      doc.setLineWidth(0.3);
      doc.line(14, pageHeight - 12, pageWidth - 14, pageHeight - 12);

      // page number
      doc.setFont('helvetica', 'normal');
      doc.setFontSize(7);
      doc.setTextColor(150, 150, 150);
      doc.text(
        `Page ${data.pageNumber} of ${doc.getNumberOfPages()}`,
        pageWidth / 2,
        pageHeight - 7,
        { align: 'center' }
      );

      // date right
      doc.text(new Date().toLocaleDateString(), pageWidth - 14, pageHeight - 7, { align: 'right' });
    },
  });

  doc.save(`Employee_Report_${new Date().toISOString().split('T')[0]}.pdf`);
};

export const calculateAge = (birthDate: string | null | undefined): number | null => {
  if (!birthDate) return null;

  const birth = new Date(birthDate);
  const today = new Date();

  let age = today.getFullYear() - birth.getFullYear();

  // adjust if birthday hasn't occurred yet this year
  const hasHadBirthdayThisYear =
    today.getMonth() > birth.getMonth() ||
    (today.getMonth() === birth.getMonth() && today.getDate() >= birth.getDate());

  if (!hasHadBirthdayThisYear) age--;

  return age;
};
