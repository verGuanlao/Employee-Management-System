import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarHeader,
} from '@/components/ui/sidebar';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from './ui/button';
import { getUserRole, logout } from '@/auth/authService';

export function AppSidebar() {
  const navigate = useNavigate();
  const role = getUserRole();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <Sidebar className="border-r bg-white">
      <SidebarHeader className="flex items-center justify-start px-4 py-6">
        <img
          src="/public/logo.svg" // adjust path to your logo file
          alt="Employee Portal Logo"
          className="h-12 w-auto"
        />
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup className="px-2">
          <nav>
            <ul className="space-y-1">
              <li>
                <Link to="/employees">
                  <Button
                    variant="ghost"
                    className="w-full justify-start rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-100"
                  >
                    Employees
                  </Button>
                </Link>
              </li>
              <li>
                <Link to="/departments">
                  <Button
                    variant="ghost"
                    className="w-full justify-start rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-100"
                  >
                    Departments
                  </Button>
                </Link>
              </li>
              {role === 'ADMIN' && (
                <li>
                  <Link to="/users">
                    <Button
                      variant="ghost"
                      className="w-full justify-start rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-100"
                    >
                      Users
                    </Button>
                  </Link>
                </li>
              )}
            </ul>
          </nav>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter className="px-4 py-4">
        <Button
          variant="destructive"
          className="w-full justify-start rounded-md px-3 py-2 text-sm font-medium"
          onClick={handleLogout}
        >
          Logout
        </Button>
      </SidebarFooter>
    </Sidebar>
  );
}
