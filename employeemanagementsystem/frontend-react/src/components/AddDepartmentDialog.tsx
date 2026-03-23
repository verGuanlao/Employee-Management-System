import { useState } from 'react';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { addDepartment, type ApiResponse, type DepartmentDTO } from '@/lib/utils';
import { toast } from 'sonner';

export default function AddDepartmentDialog({ onAdded }: { onAdded: () => void }) {
  const [open, setOpen] = useState(false);
  const [name, setName] = useState('');
  const [msg, setMsg] = useState('');

  const handleAdd = async () => {
    setMsg('');
    const dto: DepartmentDTO = { departmentId: null, departmentName: name };
    const res: ApiResponse<DepartmentDTO> = await addDepartment(dto);
    if (res.status === 'success') {
      setOpen(false);
      toast.success(res.message ?? 'Department added successfully');
      onAdded();
    } else {
      setMsg(res.message ?? 'Error adding department');
    }
  };

  return (
    <>
      <Button
        onClick={() => {
          setMsg('');
          setOpen(true);
        }}
      >
        Add Department
      </Button>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Add Department</DialogTitle>
          </DialogHeader>
          <Input
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Department name"
          />
          <div>{msg && <p className="text-sm text-red-500">{msg}</p>}</div>
          <DialogFooter>
            <Button onClick={handleAdd}>Save</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
}
