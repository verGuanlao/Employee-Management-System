import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import { Popover, PopoverTrigger, PopoverContent } from '@/components/ui/popover';
import { format, parseISO } from 'date-fns';
import { useState, useEffect } from 'react';
import {
  type EmployeeDTO,
  updateEmployee,
  fetchDepartments,
  type DepartmentDTO,
  type ApiResponse,
  type Page,
} from '@/lib/utils';
import { toast } from 'sonner';

interface Props {
  employee: EmployeeDTO;
  onUpdated?: () => void;
}

const EditEmployeeDialog: React.FC<Props> = ({ employee, onUpdated }) => {
  const [open, setOpen] = useState(false);
  const [birthDate, setBirthDate] = useState<Date | undefined>(
    employee.birthDate ? parseISO(employee.birthDate) : undefined
  );
  const [formData, setFormData] = useState<EmployeeDTO>(employee);
  const [departments, setDepartments] = useState<DepartmentDTO[]>([]);
  const [msg, setMsg] = useState(''); // ✅ added

  const today = new Date();
  const cutoff = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());

  useEffect(() => {
    async function loadDepartments() {
      const res: ApiResponse<Page<DepartmentDTO>> = await fetchDepartments({
        page: 0,
        size: 50,
      });
      if (res.status === 'success') {
        setDepartments(res.data.content);
      } else {
        setMsg(res.message ?? 'Failed to load departments');
      }
    }
    loadDepartments();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    setMsg('');

    const res: ApiResponse<EmployeeDTO> = await updateEmployee({
      ...formData,
      birthDate: birthDate ? format(birthDate, 'yyyy-MM-dd') : '',
      employeeId: formData.employeeId,
    });

    if (res.status === 'success') {
      setOpen(false);
      onUpdated?.();
      toast.success(res.message ?? 'Employee updated successfully');
    } else {
      setMsg(res.message ?? 'Failed to update employee');
    }
  };

  return (
    <Dialog
      open={open}
      onOpenChange={(isOpen) => {
        if (isOpen) {
          setFormData(employee);
          setBirthDate(employee.birthDate ? parseISO(employee.birthDate) : undefined);
          setMsg('');
        }
        setOpen(isOpen);
      }}
    >
      <DialogTrigger asChild>
        <Button variant="outline" size="sm">
          Edit
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Edit Employee</DialogTitle>
        </DialogHeader>
        <form className="grid gap-4">
          <div>
            <label className="mb-1 block font-medium">Employee ID</label>
            <p className="rounded border bg-gray-100 p-2 text-gray-600">{formData.employeeId}</p>
          </div>

          <div>
            <label className="mb-1 block font-medium">
              Name <span className="ml-1 text-red-500">*</span>
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className="rounded border p-2"
            />
          </div>

          <div>
            <label className="mb-1 block font-medium">
              Department <span className="ml-1 text-red-500">*</span>
            </label>
            <select
              name="department"
              className="w-full rounded border p-2"
              value={formData.department}
              onChange={handleChange}
            >
              <option value="">Select Department</option>
              {departments.map((dept) => (
                <option
                  key={dept.departmentId}
                  value={dept.departmentName ? dept.departmentName : ''}
                >
                  {dept.departmentName}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="mb-1 block font-medium">
              Salary <span className="ml-1 text-red-500">*</span>
            </label>
            <input
              type="number"
              name="salary"
              value={formData.salary}
              onChange={handleChange}
              className="rounded border p-2"
            />
          </div>

          <div>
            <label className="mb-1 block font-medium">
              Birthdate <span className="ml-1 text-red-500">*</span>
            </label>
            <Popover>
              <PopoverTrigger asChild>
                <Button variant="outline" className="w-full justify-start">
                  {birthDate ? format(birthDate, 'PPP') : 'Pick a date'}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="p-0">
                <Calendar
                  mode="single"
                  selected={birthDate}
                  onSelect={setBirthDate}
                  disabled={(date) => date > cutoff}
                  defaultMonth={formData.birthDate ? parseISO(formData.birthDate) : cutoff}
                  captionLayout="dropdown"
                />
              </PopoverContent>
            </Popover>
          </div>

          {msg && <p className="text-sm text-red-500">{msg}</p>}
        </form>
        <DialogFooter>
          <Button type="button" onClick={handleSubmit}>
            Save Changes
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default EditEmployeeDialog;
