import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import DashboardLayout from "@/layouts/DashboardLayout"
import AddEmployeeDialog from "@/components/AddEmployeeDialog"
import {
  HoverCard,
  HoverCardTrigger,
  HoverCardContent,
} from "@/components/ui/hover-card"
import { useEffect, useState } from "react"
import {
  fetchEmployees,
  fetchDepartments,
  searchEmployees,
  type EmployeeDTO,
  type DepartmentDTO,
  type ApiResponse,
  type EmployeeStatsResponse,
  type Page,
} from "@/lib/utils"
import EditEmployeeDialog from "@/components/EditEmployeeDialog"
import DeleteEmployeeDialog from "@/components/DeleteEmployeeDialog"
import { format } from "date-fns"

const EmployeesPage = () => {
  const [employees, setEmployees] = useState<EmployeeDTO[]>([])
  const [departments, setDepartments] = useState<DepartmentDTO[]>([])
  const [selectedDept, setSelectedDept] = useState<number | null>(null)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)
  const [totalCount, setTotalCount] = useState(0)
  const [minAge, setMinAge] = useState<number | null>(null)
  const [maxAge, setMaxAge] = useState<number | null>(null)
  const [stats, setStats] = useState<EmployeeStatsResponse["stats"] | null>(
    null
  )
  const [searchTerm, setSearchTerm] = useState("")

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

  const loadEmployees = async () => {
    const params: any = { page, size: 5 }
    if (selectedDept) params.deptId = selectedDept
    if (minAge !== null) params.minAge = minAge
    if (maxAge !== null) params.maxAge = maxAge

    if (searchTerm.trim() !== "") {
      const res: ApiResponse<EmployeeStatsResponse> = await searchEmployees(
        searchTerm,
        params
      )
      if (res.status === "success") {
        setEmployees(res.data.employees.content)
        setTotalCount(res.data.employees.totalElements)
        setTotalPages(res.data.employees.totalPages)
        setStats(null)
      }
    } else {
      const res: ApiResponse<EmployeeStatsResponse> =
        await fetchEmployees(params)
      if (res.status === "success") {
        setEmployees(res.data.employees.content)
        setStats({
          ...res.data.stats,
          averageSalary: res.data.stats.averageSalary ?? 0,
          averageAge: res.data.stats.averageAge ?? 0,
        })
        setTotalCount(res.data.employees.totalElements)
        setTotalPages(res.data.employees.totalPages)
      }
    }
  }

  // effect just calls it
  useEffect(() => {
    loadEmployees()
  }, [selectedDept, minAge, maxAge, page, searchTerm])

  const resetFilters = () => {
    setSelectedDept(null)
    setMinAge(null)
    setMaxAge(null)
    setSearchTerm("")
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
              <p className="text-2xl font-bold">{totalCount}</p>
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
          <AddEmployeeDialog
            onUpdated={() => {
              setPage(0)
              loadEmployees()
            }}
          />
        </CardHeader>
        <CardContent>
          {/* Filters + Search */}
          <div className="mb-4 grid grid-cols-6 items-end gap-4">
            <div>
              <label className="mb-2 block font-medium">Department</label>
              <select
                className="w-full rounded border p-2"
                value={selectedDept ?? ""}
                onChange={(e) => {
                  setSelectedDept(
                    e.target.value ? Number(e.target.value) : null
                  )
                  setPage(0)
                }}
              >
                <option value="">All Departments</option>
                {departments.map((dept) => (
                  <option key={dept.departmentId} value={dept.departmentId}>
                    {dept.departmentName}
                  </option>
                ))}
              </select>
            </div>
            <HoverCard
              open={minAge !== null && maxAge !== null && minAge >= maxAge}
            >
              <HoverCardTrigger asChild>
                <div className="col-span-2 flex gap-4">
                  <div className="flex-1">
                    <label className="mb-2 block font-medium">Min Age</label>
                    <input
                      type="number"
                      min={1}
                      className="w-full rounded border p-2"
                      value={minAge ?? ""}
                      onChange={(e) =>
                        setMinAge(
                          e.target.value ? Number(e.target.value) : null
                        )
                      }
                    />
                  </div>
                  <div className="flex-1">
                    <label className="mb-2 block font-medium">Max Age</label>
                    <input
                      type="number"
                      min={1}
                      className="w-full rounded border p-2"
                      value={maxAge ?? ""}
                      onChange={(e) =>
                        setMaxAge(
                          e.target.value ? Number(e.target.value) : null
                        )
                      }
                    />
                  </div>
                </div>
              </HoverCardTrigger>
              <HoverCardContent
                side="top"
                align="center"
                className="text-sm text-red-600"
              >
                Min age must be less than Max age
              </HoverCardContent>
            </HoverCard>

            <div>
              <label className="mb-2 block font-medium">Search Employee</label>
              <input
                type="text"
                className="w-full rounded border p-2"
                value={searchTerm}
                onChange={(e) => {
                  setSearchTerm(e.target.value)
                  setPage(0)
                }}
                placeholder="Enter name or ID..."
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
            <div>
              <Button
                variant="secondary"
                className="w-full"
                onClick={() => {
                  setSearchTerm("")
                  setPage(0)
                }}
              >
                Clear Search
              </Button>
            </div>
          </div>

          <div className="w-full">
            {/* Table Header */}
            <div className="grid grid-cols-6 gap-4 border-b bg-gray-100 px-3 py-2 font-semibold">
              <div className="truncate">Employee ID</div>
              <div className="truncate">Name</div>
              <div className="truncate">Department</div>
              <div className="truncate">Birthdate</div>
              <div className="truncate">Salary</div>
              <div className="mr-3 flex justify-end pr-6">Actions</div>
            </div>

            {/* Table Body */}
            <div className="divide-y">
              {employees.map((emp) => (
                <div
                  key={emp.employeeId}
                  className="grid grid-cols-6 items-center gap-4 px-4 py-2"
                >
                  <div className="truncate">{emp.employeeId}</div>
                  <div className="truncate font-medium">{emp.name}</div>
                  <div className="truncate text-gray-500">{emp.department}</div>
                  <div className="truncate">
                    {emp.birthDate
                      ? format(new Date(emp.birthDate), "MM-dd-yy")
                      : "-"}
                  </div>
                  <div className="truncate">₱{emp.salary.toLocaleString()}</div>
                  <div className="flex justify-end gap-2">
                    <EditEmployeeDialog
                      employee={emp}
                      onUpdated={() => {
                        setPage(0)
                        loadEmployees()
                      }}
                    />
                    <DeleteEmployeeDialog
                      employee={emp}
                      onDeleted={loadEmployees}
                    />
                  </div>
                </div>
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
    </DashboardLayout>
  )
}

export default EmployeesPage
