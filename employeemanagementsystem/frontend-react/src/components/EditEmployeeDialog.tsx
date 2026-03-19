import {
  Dialog,
  DialogContent,
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

interface Props {
  employee: EmployeeDTO;
  onUpdated?: () => void;
}

const EditEmployeeDialog: React.FC<Props> = ({ employee, onUpdated }) => {
  const [open, setOpen] = useState(false);

  // convert existing birthDate string to Date
  const [birthDate, setBirthDate] = useState<Date | undefined>(
    employee.birthDate ? parseISO(employee.birthDate) : undefined
  );
  const [formData, setFormData] = useState<EmployeeDTO>(employee);

  const [departments, setDepartments] = useState<DepartmentDTO[]>([]);

  // Calculate cutoff date for 18 years old
  const today = new Date();
  const cutoff = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
  const cutoffISO = cutoff.toISOString().split('T')[0]; // format YYYY-MM-DD

  useEffect(() => {
    async function loadDepartments() {
      const res: ApiResponse<Page<DepartmentDTO>> = await fetchDepartments({
        page: 0,
        size: 50,
      });
      if (res.status === 'success') {
        setDepartments(res.data.content);
      }
    }
    loadDepartments();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    if (!formData.name || !birthDate || !formData.salary || !formData.department) {
      alert('Please fill out all fields before saving.');
      return;
    }

    if (formData.salary <= 0) {
      alert('Salary must be a positive number.');
      return;
    }

    const birthDateString = format(birthDate, 'yyyy-MM-dd');

    await updateEmployee({
      ...formData,
      birthDate: birthDateString,
      employeeId: formData.employeeId,
    });

    setOpen(false);
    if (onUpdated) onUpdated();
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
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
          {/* Employee ID (read-only) */}
          <div>
            <label className="mb-1 block font-medium">Employee ID</label>
            <p className="rounded border bg-gray-100 p-2 text-gray-600">{formData.employeeId}</p>
          </div>

          {/* Name */}
          <div>
            <label className="mb-1 block font-medium">
              Name
              <span className="ml-1 text-red-500">*</span>
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className="rounded border p-2"
            />
          </div>

          {/* Department Dropdown */}
          <div>
            <label className="mb-1 block font-medium">
              Department
              <span className="ml-1 text-red-500">*</span>
            </label>
            <select
              name="department"
              className="w-full rounded border p-2"
              value={formData.department}
              onChange={handleChange}
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
            <label className="mb-1 block font-medium">
              Salary
              <span className="ml-1 text-red-500">*</span>
            </label>
            <input
              type="number"
              name="salary"
              value={formData.salary}
              onChange={handleChange}
              className="rounded border p-2"
            />
          </div>

          {/* Birthdate with Calendar */}
          <div>
            <label className="mb-1 block font-medium">
              Birthdate
              <span className="ml-1 text-red-500">*</span>
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

          <Button type="button" onClick={handleSubmit}>
            Save Changes
          </Button>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default EditEmployeeDialog;
