import { useState } from 'react';
import { updateUser, type UserDTO, type ApiResponse } from '@/lib/utils';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { set } from 'date-fns';
import { getUsername } from '@/auth/tokenUtils';
import { toast } from 'sonner';

const EditUserDialog = ({ user, onUpdated }: { user: UserDTO; onUpdated: () => void }) => {
  const [open, setOpen] = useState(false);
  const [username, setUsername] = useState(user.username);
  const [role, setRole] = useState(user.role);
  const [msg, setMsg] = useState('');

  const currentUsername = getUsername();
  const isSelf = currentUsername === user.username;

  const handleSubmit = async () => {
    setMsg('');
    const res: ApiResponse<UserDTO> = await updateUser({
      userId: user.userId,
      username,
      password: '',
      role,
    });
    if (res.status === 'success') {
      setOpen(false);
      onUpdated();
      toast.success(res.message ?? 'User updated successfully');
    } else {
      setMsg(res.message ?? 'Failed to update user');
    }
  };

  return (
    <>
      <Button
        variant="outline"
        onClick={() => {
          setMsg('');
          setUsername(user.username ?? '');
          setRole(user.role ?? 'USER');
          setOpen(true);
        }}
      >
        Edit
      </Button>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Edit New User</DialogTitle>
          </DialogHeader>
          <div className="flex flex-col gap-3 py-2">
            <div className="flex flex-col gap-1">
              <Label>Username</Label>
              <Input value={username} onChange={(e) => setUsername(e.target.value)} />
            </div>
            <div className="flex flex-col gap-1">
              <Label>Role</Label>
              <Select value={role} onValueChange={setRole} disabled={isSelf}>
                <SelectTrigger>
                  <SelectValue placeholder="Select role" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="USER">USER</SelectItem>
                  <SelectItem value="ADMIN">ADMIN</SelectItem>
                </SelectContent>
              </Select>
            </div>
            {msg && <p className="text-sm text-red-500">{msg}</p>}
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setOpen(false)}>
              Cancel
            </Button>
            <Button onClick={handleSubmit}>Save</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
};

export default EditUserDialog;
