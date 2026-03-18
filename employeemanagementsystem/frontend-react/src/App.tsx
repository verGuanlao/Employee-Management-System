import { BrowserRouter, Routes, Route } from "react-router-dom"
import EmployeesPage from "./pages/EmployeesPage"
import DepartmentsPage from "./pages/DepartmentsPage"
import LoginPage from "./pages/LoginPage"
import PrivateRoute from "./auth/PrivateRoute"

export function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/employees"
          element={
            <PrivateRoute>
              <EmployeesPage />
            </PrivateRoute>
          }
        />
        <Route
          path="/departments"
          element={
            <PrivateRoute>
              <DepartmentsPage />
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  )
}

export default App
