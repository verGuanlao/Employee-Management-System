import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
  DialogTrigger,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Calendar } from '@/components/ui/calendar';
import { Popover, PopoverTrigger, PopoverContent } from '@/components/ui/popover';
import { format } from 'date-fns';
import { useState, useEffect } from 'react';
import {
  addEmployee,
  fetchDepartments,
  type DepartmentDTO,
  type ApiResponse,
  type Page,
  type EmployeeDTO,
} from '@/lib/utils';
import { toast } from 'sonner';

interface Props {
  onUpdated?: () => void;
}

export default function AddEmployeeDialog({ onUpdated }: Props) {
  const [isOpen, setIsOpen] = useState(false);
  const [name, setName] = useState('');
  const [salary, setSalary] = useState<number | ''>('');
  const [birthDate, setBirthDate] = useState<Date | undefined>(undefined);
  const [departments, setDepartments] = useState<DepartmentDTO[]>([]);
  const [selectedDept, setSelectedDept] = useState<string>('');
  const [msg, setMsg] = useState('');

  // Calculate cutoff date for 18 years old
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

  const handleSubmit = async () => {
    setMsg('');
    const res: ApiResponse<EmployeeDTO> = await addEmployee({
      employeeId: null,
      name,
      birthDate: birthDate ? format(birthDate, 'yyyy-MM-dd') : '',
      salary: Number(salary),
      department: selectedDept,
    });

    if (res.status == 'success') {
      setName('');
      setSalary('');
      setBirthDate(undefined);
      setSelectedDept('');
      setIsOpen(false);
      if (onUpdated) onUpdated();
      toast.success(res.message ?? 'Employee added successfully');
    } else {
      setMsg(res.message ?? 'Failed to add employee');
    }
  };

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
                <option
                  key={dept.departmentId}
                  value={dept.departmentName ? dept.departmentName : ''}
                >
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
              onChange={(e) => setSalary(e.target.value ? Number(e.target.value) : '')}
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
                  {birthDate ? format(birthDate, 'PPP') : 'Pick a date'}
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
        <div>{msg && <p className="text-sm text-red-500">{msg}</p>}</div>
        <DialogFooter>
          <Button onClick={handleSubmit}>Save</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
