import {
  SidebarInset,
  SidebarProvider,
  SidebarTrigger,
} from "@/components/ui/sidebar"
import { AppSidebar } from "@/components/AppSidebar"
import { AppPagination } from "@/components/AppPagination"
import type { ReactNode } from "react"

interface DashboardLayoutProps {
  children: ReactNode
  page: number
  totalPages: number
  onPageChange: (page: number) => void
}

export default function DashboardLayout({
  children,
  page,
  totalPages,
  onPageChange,
}: DashboardLayoutProps) {
  return (
    <SidebarProvider
      style={
        {
          "--sidebar-width": "10rem",
        } as React.CSSProperties
      }
    >
      <AppSidebar />
      <SidebarInset>
        <main className="p-4">
          <SidebarTrigger />
          <div className="flex-1 p-6">{children}</div>
        </main>
        <div className="mt-6 flex justify-center pb-7">
          <AppPagination
            page={page}
            totalPages={totalPages}
            onPageChange={onPageChange}
          />
        </div>
      </SidebarInset>
    </SidebarProvider>
  )
}
