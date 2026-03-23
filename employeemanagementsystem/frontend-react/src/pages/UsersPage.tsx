import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import DashboardLayout from '@/layouts/DashboardLayout';
import { useEffect, useState } from 'react';
import { getAllUsers, type UserDTO, type Page, type ApiResponse } from '@/lib/utils';
import AddUserDialog from '@/components/AddUserDialog';
import EditUserDialog from '@/components/EditUserDialog';
import DeleteUserDialog from '@/components/DeleteUserDialog';
import { toast } from 'sonner';

const UsersPage = () => {
  const [users, setUsers] = useState<UserDTO[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const loadUsers = async () => {
    const res: ApiResponse<Page<UserDTO>> = await getAllUsers({
      username: searchQuery.trim() !== '' ? searchQuery : undefined,
      page,
      size: 5,
    });

    if (res.status === 'success') {
      setUsers(res.data.content);
      setTotalPages(res.data.totalPages);
    } else {
      toast.error(res.message ?? 'Error loading users');
    }
  };

  useEffect(() => {
    const timeout = setTimeout(() => {
      loadUsers();
    }, 100);
    return () => clearTimeout(timeout);
  }, [searchQuery, page]);

  return (
    <DashboardLayout page={page} totalPages={totalPages} onPageChange={setPage}>
      <Card>
        <CardHeader className="flex items-center justify-between">
          <CardTitle>User List</CardTitle>
          <AddUserDialog onAdded={loadUsers} />
        </CardHeader>
        <CardContent>
          <div className="mb-4">
            <input
              type="text"
              placeholder="Search users..."
              className="w-full rounded border p-2"
              value={searchQuery}
              onChange={(e) => {
                setSearchQuery(e.target.value);
                setPage(0);
              }}
            />
          </div>

          <ul className="divide-y">
            {users.map((user) => (
              <li key={user.userId} className="flex items-center justify-between py-2">
                <div className="flex flex-col">
                  <span className="font-medium">{user.username}</span>
                  <span className="text-xs text-muted-foreground">{user.role}</span>
                </div>
                <div className="flex gap-2">
                  <EditUserDialog user={user} onUpdated={loadUsers} />
                  <DeleteUserDialog user={user} onDeleted={loadUsers} />
                </div>
              </li>
            ))}
          </ul>
        </CardContent>
      </Card>
    </DashboardLayout>
  );
};

export default UsersPage;
