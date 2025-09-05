// src/api.js
import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL, // must be http://localhost:8080/api
  headers: { "Content-Type": "application/json" },
});

export default api;
