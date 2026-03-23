import { useState } from 'react';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { deleteDepartment, type ApiResponse, type DepartmentDTO } from '@/lib/utils';
import { toast } from 'sonner';

export default function DeleteDepartmentDialog({
  department,
  onDeleted,
}: {
  department: DepartmentDTO;
  onDeleted: () => void;
}) {
  const [open, setOpen] = useState(false);

  const handleDelete = async () => {
    const res: ApiResponse<DepartmentDTO> = await deleteDepartment(department);
    if (res.status === 'success') {
      setOpen(false);
      onDeleted();
      toast.success(res.message ?? 'Department deleted successfully');
    } else {
      toast.error(res.message ?? 'Error deleting department');
    }
  };

  return (
    <>
      <Button variant="destructive" onClick={() => setOpen(true)}>
        Delete
      </Button>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Delete Department</DialogTitle>
          </DialogHeader>
          <p>
            Are you sure you want to delete{' '}
            <span className="font-semibold">{department.departmentName}?</span>
          </p>
          <DialogFooter>
            <Button variant="destructive" onClick={handleDelete}>
              Confirm
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
}
