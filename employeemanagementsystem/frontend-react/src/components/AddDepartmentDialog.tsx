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

export default function AddDepartmentDialog({ onAdded }: { onAdded: () => void }) {
  const [open, setOpen] = useState(false);
  const [name, setName] = useState('');

  const handleAdd = async () => {
    const dto: DepartmentDTO = { departmentId: null, departmentName: name };
    const res: ApiResponse<DepartmentDTO> = await addDepartment(dto);
    if (res.status === 'success') {
      setOpen(false);
      onAdded();
    } else {
      alert(res.message ?? 'Error adding department');
    }
  };

  return (
    <>
      <Button onClick={() => setOpen(true)}>Add Department</Button>
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
          <DialogFooter>
            <Button onClick={handleAdd}>Save</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
}
