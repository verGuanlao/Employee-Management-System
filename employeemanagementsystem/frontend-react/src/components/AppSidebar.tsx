import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarHeader,
} from "@/components/ui/sidebar"
import { Link } from "react-router-dom"
import { Button } from "./ui/button"

export function AppSidebar() {
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
      <SidebarFooter />
    </Sidebar>
  )
}
