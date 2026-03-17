import { BrowserRouter, Routes, Route } from "react-router-dom"
import EmployeesPage from "./pages/EmployeesPage"
import DepartmentsPage from "./pages/DepartmentsPage"

export function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/employees" element={<EmployeesPage />} />
        <Route path="/departments" element={<DepartmentsPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
