import { useState } from 'react';
import { deleteUser, type UserDTO, type ApiResponse } from '@/lib/utils';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { toast } from 'sonner';
import { getUsername } from '@/auth/tokenUtils';

const DeleteUserDialog = ({ user, onDeleted }: { user: UserDTO; onDeleted: () => void }) => {
  const [open, setOpen] = useState(false);
  const [error, setError] = useState('');
  const currentUsername = getUsername();
  const isSelf = currentUsername === user.username;

  const handleDelete = async () => {
    setError('');
    const res: ApiResponse<UserDTO> = await deleteUser({
      userId: user.userId,
      username: user.username,
      password: '',
      role: user.role,
    });
    if (res.status === 'success') {
      setOpen(false);
      onDeleted();
      toast.success(res.message ?? 'User deleted successfully');
    } else {
      toast.error(res.message ?? 'Failed to delete user');
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="destructive" size="sm">
          Delete
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Delete User</DialogTitle>
        </DialogHeader>
        <p>
          Are you sure you want to delete <span className="font-semibold">{user.username}</span>?
        </p>
        <DialogFooter>
          <Button variant="outline" onClick={() => setOpen(false)}>
            Cancel
          </Button>
          <Button variant="destructive" onClick={handleDelete} disabled={isSelf}>
            Delete
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default DeleteUserDialog;
