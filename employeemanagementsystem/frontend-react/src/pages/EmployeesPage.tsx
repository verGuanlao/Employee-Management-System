import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import DashboardLayout from "@/layouts/DashboardLayout"
import AddEmployeeDialog from "@/components/AddEmployeeDialog"
import { useEffect, useState } from "react"
import {
  fetchEmployees,
  fetchDepartments,
  type EmployeeDTO,
  type DepartmentDTO,
  type ApiResponse,
  type EmployeeStatsResponse,
  type Page,
} from "@/lib/utils"

const EmployeesPage = () => {
  const [employees, setEmployees] = useState<EmployeeDTO[]>([])
  const [departments, setDepartments] = useState<DepartmentDTO[]>([])
  const [selectedDept, setSelectedDept] = useState<number | null>(null)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)
  const [minAge, setMinAge] = useState<number | null>(null)
  const [maxAge, setMaxAge] = useState<number | null>(null)
  const [stats, setStats] = useState<EmployeeStatsResponse["stats"] | null>(
    null
  )

  useEffect(() => {
    async function loadDepartments() {
      const res: ApiResponse<Page<DepartmentDTO>> = await fetchDepartments({
        page: 0,
        size: 50,
      })
      if (res.status === "success") setDepartments(res.data.content)
    }
    loadDepartments()
  }, [])

  useEffect(() => {
    async function loadEmployees() {
      const params: any = { page, size: 5 }
      if (selectedDept) params.deptId = selectedDept
      if (minAge !== null) params.minAge = minAge
      if (maxAge !== null) params.maxAge = maxAge

      const res: ApiResponse<EmployeeStatsResponse> =
        await fetchEmployees(params)
      if (res.status === "success") {
        setEmployees(res.data.employees.content)
        setStats(res.data.stats)
        setTotalPages(res.data.employees.totalPages)
      }
    }
    loadEmployees()
  }, [selectedDept, minAge, maxAge, page])

  const resetFilters = () => {
    setSelectedDept(null)
    setMinAge(null)
    setMaxAge(null)
  }

  return (
    <DashboardLayout page={page} totalPages={totalPages} onPageChange={setPage}>
      {/* Stats */}
      {stats && (
        <div className="mb-6 grid grid-cols-3 gap-4">
          <Card>
            <CardHeader>
              <CardTitle>Total Employees</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-2xl font-bold">{stats.totalCount}</p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader>
              <CardTitle>Average Salary</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-2xl font-bold">
                ₱
                {stats.averageSalary.toLocaleString("en-US", {
                  minimumFractionDigits: 2,
                  maximumFractionDigits: 2,
                })}
              </p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader>
              <CardTitle>Average Age</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-2xl font-bold">
                {stats.averageAge.toFixed(1)}
              </p>
            </CardContent>
          </Card>
        </div>
      )}

      {/* Employee List */}
      <Card>
        <CardHeader className="flex items-center gap-4">
          <CardTitle>Employee List</CardTitle>
          <AddEmployeeDialog />
        </CardHeader>
        <CardContent>
          {/* Filters */}
          <div className="mb-4 grid grid-cols-4 items-end gap-4">
            <div>
              <label className="mb-2 block font-medium">Department</label>
              <select
                className="w-full rounded border p-2"
                value={selectedDept ?? ""}
                onChange={(e) =>
                  setSelectedDept(
                    e.target.value ? Number(e.target.value) : null
                  )
                }
              >
                <option value="">All Departments</option>
                {departments.map((dept) => (
                  <option key={dept.departmentId} value={dept.departmentId}>
                    {dept.departmentName}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="mb-2 block font-medium">Min Age</label>
              <input
                type="number"
                min={0}
                className="w-full rounded border p-2"
                value={minAge ?? ""}
                onChange={(e) =>
                  setMinAge(e.target.value ? Number(e.target.value) : null)
                }
              />
            </div>
            <div>
              <label className="mb-2 block font-medium">Max Age</label>
              <input
                type="number"
                min={0}
                className="w-full rounded border p-2"
                value={maxAge ?? ""}
                onChange={(e) =>
                  setMaxAge(e.target.value ? Number(e.target.value) : null)
                }
              />
            </div>
            <div>
              <Button
                variant="outline"
                className="w-full"
                onClick={resetFilters}
              >
                Reset Filters
              </Button>
            </div>
          </div>

          <ul className="divide-y">
            {employees.map((emp) => (
              <li key={emp.employeeId} className="flex justify-between py-2">
                <span>{emp.name}</span>
                <span className="text-gray-500">{emp.department}</span>
              </li>
            ))}
          </ul>
        </CardContent>
      </Card>
    </DashboardLayout>
  )
}

export default EmployeesPage
