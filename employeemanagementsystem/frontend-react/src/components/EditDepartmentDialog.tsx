import { useState } from "react"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import {
  updateDepartment,
  type ApiResponse,
  type DepartmentDTO,
} from "@/lib/utils"

export default function EditDepartmentDialog({
  department,
  onUpdated,
}: {
  department: DepartmentDTO
  onUpdated: () => void
}) {
  const [open, setOpen] = useState(false)
  const [name, setName] = useState(department.departmentName ?? "")

  const handleUpdate = async () => {
    const dto: DepartmentDTO = { ...department, departmentName: name }
    const res: ApiResponse<DepartmentDTO> = await updateDepartment(dto)
    if (res.status === "success") {
      setOpen(false)
      onUpdated()
    } else {
      alert(res.message ?? "Error updating department")
    }
  }

  return (
    <>
      <Button variant="outline" onClick={() => setOpen(true)}>
        Edit
      </Button>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Edit Department</DialogTitle>
          </DialogHeader>
          <Input value={name} onChange={(e) => setName(e.target.value)} />
          <DialogFooter>
            <Button onClick={handleUpdate}>Save</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  )
}
