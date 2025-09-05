import React, { useState } from "react";
import api from "./api";

export default function Login() {
  const [usernameOrEmail, setUsernameOrEmail] = useState("");
  const [password, setPassword] = useState("");

  async function handleLogin(e) {
    e.preventDefault();
    try {
      const res = await api.post("/auth/login", {
        usernameOrEmail,
        password,
      });
      localStorage.setItem("token", res.data.accessToken); // save JWT
      alert("Login successful!");
      window.location.href = "/notes"; // redirect after login
    } catch (err) {
      alert("Login failed: " + err.message);
      console.error(err);
    }
  }

  return (
    <form onSubmit={handleLogin}>
      <input
        placeholder="Username or Email"
        value={usernameOrEmail}
        onChange={(e) => setUsernameOrEmail(e.target.value)}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button type="submit">Login</button>
    </form>
  );
}
