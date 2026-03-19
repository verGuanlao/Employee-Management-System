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

const DeleteUserDialog = ({ user, onDeleted }: { user: UserDTO; onDeleted: () => void }) => {
  const [open, setOpen] = useState(false);
  const [error, setError] = useState('');

  const handleDelete = async () => {
    setError('');
    const res: ApiResponse<UserDTO> = await deleteUser({
      userId: user.userId,
      username: user.username,
      role: user.role,
    });
    if (res.status === 'success') {
      setOpen(false);
      onDeleted();
    } else {
      setError(res.message ?? 'Failed to delete user');
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
        <p className="text-sm text-muted-foreground">
          Are you sure you want to delete <span className="font-semibold">{user.username}</span>?
          This action cannot be undone.
        </p>
        {error && <p className="text-sm text-red-500">{error}</p>}
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

export default DeleteUserDialog;
