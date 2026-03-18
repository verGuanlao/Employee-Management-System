export const login = async (username: string, password: string) => {
  const res = await fetch("http://localhost:8080/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  })

  const body = await res.json()
  console.log("Response body:", body)
  console.log("Response status:", res.status)
  console.log("Authorization header:", res.headers.get("Authorization"))

  if (!res.ok) throw new Error("Login failed")

  // ✅ Read token from response header
  const authHeader = res.headers.get("Authorization")
  const token = authHeader?.replace("Bearer ", "")

  if (!token) throw new Error("No token received")

  localStorage.setItem("token", token)
  return token
}

export const logout = () => {
  localStorage.removeItem("token")
}
