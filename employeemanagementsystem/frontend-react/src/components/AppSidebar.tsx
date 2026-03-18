import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarHeader,
} from "@/components/ui/sidebar"
import { Link, useNavigate } from "react-router-dom"
import { Button } from "./ui/button"
import { logout } from "@/auth/authService" // adjust path if needed

export function AppSidebar() {
  const navigate = useNavigate()

  const handleLogout = () => {
    logout() // clear token from localStorage
    navigate("/login") // redirect to login page
  }

  return (
    <Sidebar>
      <SidebarHeader />
      <SidebarContent>
        <SidebarGroup />
        <nav>
          <ul className="space-y-2">
            <li>
              <Link to="/employees">
                <Button variant="ghost" className="w-full justify-start">
                  Employees
                </Button>
              </Link>
            </li>
            <li>
              <Link to="/departments">
                <Button variant="ghost" className="w-full justify-start">
                  Departments
                </Button>
              </Link>
            </li>
          </ul>
        </nav>
        <SidebarGroup />
      </SidebarContent>
      <SidebarFooter>
        <Button
          variant="destructive"
          className="w-full justify-start"
          onClick={handleLogout}
        >
          Logout
        </Button>
      </SidebarFooter>
    </Sidebar>
  )
}
