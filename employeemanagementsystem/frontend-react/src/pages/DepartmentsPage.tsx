import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import DashboardLayout from '@/layouts/DashboardLayout';
import { useEffect, useState } from 'react';
import { searchDepartments, type DepartmentDTO, type Page, type ApiResponse } from '@/lib/utils';
import AddDepartmentDialog from '@/components/AddDepartmentDialog';
import EditDepartmentDialog from '@/components/EditDepartmentDialog';
import DeleteDepartmentDialog from '@/components/DeleteDepartmentDialog';
import { toast } from 'sonner';

const DepartmentsPage = () => {
  const [departments, setDepartments] = useState<DepartmentDTO[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const loadDepartments = async () => {
    const res: ApiResponse<Page<DepartmentDTO>> =
      searchQuery.trim() !== ''
        ? await searchDepartments(searchQuery, { page, size: 5 })
        : await searchDepartments('', { page, size: 5 });

    if (res.status === 'success') {
      setDepartments(res.data.content);
      setTotalPages(res.data.totalPages);
    } else {
      toast.error(res.message ?? 'Error loading departments');
    }
  };

  useEffect(() => {
    const timeout = setTimeout(() => {
      loadDepartments();
    }, 100); // debounce
    return () => clearTimeout(timeout);
  }, [searchQuery, page]);

  return (
    <DashboardLayout page={page} totalPages={totalPages} onPageChange={setPage}>
      <Card>
        <CardHeader className="flex items-center justify-between">
          <CardTitle>Department List</CardTitle>
          <AddDepartmentDialog onAdded={loadDepartments} />
        </CardHeader>
        <CardContent>
          <div className="mb-4">
            <input
              type="text"
              placeholder="Search departments..."
              className="w-full rounded border p-2"
              value={searchQuery}
              onChange={(e) => {
                setSearchQuery(e.target.value);
                setPage(0);
              }}
            />
          </div>

          <ul className="divide-y">
            {departments.map((dept) => (
              <li key={dept.departmentId} className="flex items-center justify-between py-2">
                <span>{dept.departmentName}</span>
                <div className="flex gap-2">
                  <EditDepartmentDialog department={dept} onUpdated={loadDepartments} />
                  <DeleteDepartmentDialog department={dept} onDeleted={loadDepartments} />
                </div>
              </li>
            ))}
          </ul>
        </CardContent>
      </Card>
    </DashboardLayout>
  );
};

export default DepartmentsPage;
