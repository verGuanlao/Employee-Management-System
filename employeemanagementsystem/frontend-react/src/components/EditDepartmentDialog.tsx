import { useEffect, useState } from 'react';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { updateDepartment, type ApiResponse, type DepartmentDTO } from '@/lib/utils';
import { toast } from 'sonner';

export default function EditDepartmentDialog({
  department,
  onUpdated,
}: {
  department: DepartmentDTO;
  onUpdated: () => void;
}) {
  const [open, setOpen] = useState(false);
  const [name, setName] = useState(department.departmentName ?? '');
  const [msg, setMsg] = useState('');

  const handleUpdate = async () => {
    setMsg('');
    const dto: DepartmentDTO = { ...department, departmentName: name };
    const res: ApiResponse<DepartmentDTO> = await updateDepartment(dto);
    if (res.status === 'success') {
      setOpen(false);
      onUpdated();
      toast.success(res.message ?? 'Department updated successfully');
    } else {
      setMsg(res.message ?? 'Error updating department');
    }
  };

  return (
    <>
      <Button
        variant="outline"
        onClick={() => {
          setMsg('');
          setName(department.departmentName ?? '');
          setOpen(true);
        }}
      >
        Edit
      </Button>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Edit Department</DialogTitle>
          </DialogHeader>
          <Input value={name} onChange={(e) => setName(e.target.value)} />
          <div>{msg && <p className="text-sm text-red-500">{msg}</p>}</div>
          <DialogFooter>
            <Button onClick={handleUpdate}>Save</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
}
