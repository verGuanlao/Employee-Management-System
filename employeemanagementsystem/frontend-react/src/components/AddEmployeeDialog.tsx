import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Calendar } from "@/components/ui/calendar"
import {
  Popover,
  PopoverTrigger,
  PopoverContent,
} from "@/components/ui/popover"
import { format } from "date-fns"
import { useState, useEffect } from "react"
import {
  addEmployee,
  fetchDepartments,
  type DepartmentDTO,
  type ApiResponse,
  type Page,
} from "@/lib/utils"

export default function AddEmployeeDialog() {
  const [isOpen, setIsOpen] = useState(false)
  const [name, setName] = useState("")
  const [salary, setSalary] = useState<number | "">("")
  const [birthDate, setBirthDate] = useState<Date | undefined>(undefined)
  const [departments, setDepartments] = useState<DepartmentDTO[]>([])
  const [selectedDept, setSelectedDept] = useState<string>("")

  // Calculate cutoff date for 18 years old
  const today = new Date()
  const cutoff = new Date(
    today.getFullYear() - 18,
    today.getMonth(),
    today.getDate()
  )
  const cutoffISO = cutoff.toISOString().split("T")[0] // format YYYY-MM-DD

  // 🔑 Fetch departments on mount
  useEffect(() => {
    async function loadDepartments() {
      const res: ApiResponse<Page<DepartmentDTO>> = await fetchDepartments({
        page: 0,
        size: 50,
      })
      if (res.status === "success") {
        setDepartments(res.data.content)
      }
    }
    loadDepartments()
  }, [])

  const handleSubmit = async () => {
    if (!name || !birthDate || !salary || !selectedDept) {
      alert("Please fill out all fields before saving.")
      return
    }

    if (salary <= 0) {
      alert("Salary must be a positive number.")
      return
    }

    const birthDateString = format(birthDate, "yyyy-MM-dd")

    await addEmployee({
      employeeId: null,
      name,
      birthDate: birthDateString,
      salary: Number(salary),
      department: selectedDept,
    })

    // reset form
    setName("")
    setSalary("")
    setBirthDate(undefined)
    setSelectedDept("")

    setIsOpen(false)
  }

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button onClick={() => setIsOpen(true)}>+</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Create New Employee</DialogTitle>
        </DialogHeader>

        <form className="space-y-4">
          {/* Name */}
          <div>
            <label className="mb-1 block">
              Name
              <span className="ml-1 text-red-500">*</span>
            </label>
            <Input value={name} onChange={(e) => setName(e.target.value)} />
          </div>

          {/* Department Dropdown */}
          <div>
            <label className="mb-1 block">
              Department
              <span className="ml-1 text-red-500">*</span>
            </label>
            <select
              className="w-full rounded border p-2"
              value={selectedDept}
              onChange={(e) => setSelectedDept(e.target.value)}
            >
              <option value="">Select Department</option>
              {departments.map((dept) => (
                <option key={dept.departmentId} value={dept.departmentName}>
                  {dept.departmentName}
                </option>
              ))}
            </select>
          </div>

          {/* Salary */}
          <div>
            <label className="mb-1 block">
              Salary
              <span className="ml-1 text-red-500">*</span>
            </label>
            <Input
              type="number"
              min="1"
              value={salary}
              onChange={(e) =>
                setSalary(e.target.value ? Number(e.target.value) : "")
              }
            />
          </div>

          {/* Birthdate with Calendar */}
          <div>
            <label className="mb-1 block">
              Birthdate
              <span className="ml-1 text-red-500">*</span>
            </label>
            <Popover>
              <PopoverTrigger asChild>
                <Button variant="outline" className="w-full justify-start">
                  {birthDate ? format(birthDate, "PPP") : "Pick a date"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="p-0">
                <Calendar
                  mode="single"
                  selected={birthDate}
                  onSelect={setBirthDate}
                  disabled={(date) => date > cutoff}
                  defaultMonth={cutoff}
                  captionLayout="dropdown"
                />
              </PopoverContent>
            </Popover>
          </div>
        </form>

        <DialogFooter>
          <Button onClick={handleSubmit}>Save</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
