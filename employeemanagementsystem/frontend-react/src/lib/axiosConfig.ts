// src/api/axiosConfig.ts
import axios from "axios"
import { jwtDecode } from "jwt-decode"

interface JwtPayload {
  exp: number
  sub: string
  role?: string
}

export const getToken = () => localStorage.getItem("token")

export const isTokenValid = (): boolean => {
  const token = getToken()
  if (!token) return false

  try {
    const decoded: JwtPayload = jwtDecode(token)
    return decoded.exp > Date.now() / 1000
  } catch {
    return false
  }
}

export const getCurrentUser = (): JwtPayload | null => {
  const token = getToken()
  if (!token) return null
  try {
    return jwtDecode<JwtPayload>(token)
  } catch {
    return null
  }
}

const api = axios.create({
  baseURL: "http://localhost:8080",
})

// Attach token to every request
api.interceptors.request.use((config) => {
  const token = getToken()
  if (token && isTokenValid()) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api
