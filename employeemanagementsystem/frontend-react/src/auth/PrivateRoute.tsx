// src/auth/PrivateRoute.tsx
import { Navigate } from "react-router-dom"
import { isTokenValid } from "./tokenUtils"
import type { JSX } from "react/jsx-runtime"

const PrivateRoute = ({ children }: { children: JSX.Element }) => {
  return isTokenValid() ? children : <Navigate to="/login" replace />
}

export default PrivateRoute
