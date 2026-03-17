import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import DashboardLayout from "@/layouts/DashboardLayout"
import { useEffect, useState } from "react"
import {
  searchDepartments,
  type DepartmentDTO,
  type Page,
  type ApiResponse,
} from "@/lib/utils"

const DepartmentsPage = () => {
  const [departments, setDepartments] = useState<DepartmentDTO[]>([])
  const [searchQuery, setSearchQuery] = useState("")
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)

  useEffect(() => {
    const timeout = setTimeout(async () => {
      const res: ApiResponse<Page<DepartmentDTO>> =
        searchQuery.trim() !== ""
          ? await searchDepartments(searchQuery, { page: 0, size: 5 })
          : await searchDepartments("", { page: 0, size: 5 })

      if (res.status === "success") {
        setDepartments(res.data.content)
        setTotalPages(res.data.totalPages)
      }
    }, 300) // debounce
    return () => clearTimeout(timeout)
  }, [searchQuery])

  return (
    <DashboardLayout page={page} totalPages={totalPages} onPageChange={setPage}>
      <Card>
        <CardHeader>
          <CardTitle>Department List</CardTitle>
        </CardHeader>
        <CardContent>
          {/* Search Field */}
          <div className="mb-4">
            <input
              type="text"
              placeholder="Search departments..."
              className="w-full rounded border p-2"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>

          <ul className="divide-y">
            {departments.map((dept) => (
              <li key={dept.departmentId} className="flex justify-between py-2">
                <span>{dept.departmentName}</span>
              </li>
            ))}
          </ul>
        </CardContent>
      </Card>
    </DashboardLayout>
  )
}

export default DepartmentsPage
