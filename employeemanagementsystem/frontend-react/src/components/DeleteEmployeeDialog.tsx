import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
  DialogTrigger,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { useState } from 'react';
import { type ApiResponse, type EmployeeDTO, deleteEmployee } from '@/lib/utils';
import { toast } from 'sonner';

interface Props {
  employee: EmployeeDTO;
  onDeleted?: () => void; // callback to refresh list/stats
}

const DeleteEmployeeDialog: React.FC<Props> = ({ employee, onDeleted }) => {
  const [open, setOpen] = useState(false);

  const handleDelete = async () => {
    const res: ApiResponse<EmployeeDTO> = await deleteEmployee(employee);
    if (res.status === 'success') {
      setOpen(false);
      onDeleted?.();
      toast.success(res.message ?? 'Employee deleted successfully');
    } else {
      toast.error(res.message ?? 'Failed to delete employee');
    }
  };

  return (
    <Dialog open={open} onOpenChange={(isOpen) => setOpen(isOpen)}>
      <DialogTrigger asChild>
        <Button variant="destructive" size="sm">
          Delete
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Confirm Delete</DialogTitle>
        </DialogHeader>
        <p>
          Are you sure you want to delete employee: <br />
          <span className="font-semibold">
            {employee.name} (ID: {employee.employeeId})
          </span>
        </p>
        <DialogFooter>
          <Button variant="outline" onClick={() => setOpen(false)}>
            Cancel
          </Button>
          <Button variant="destructive" onClick={handleDelete}>
            Delete
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default DeleteEmployeeDialog;
