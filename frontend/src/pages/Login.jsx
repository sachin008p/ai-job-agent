import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import api from "../api";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);
    try {
      const response = await api.post("/auth/login", { email, password });
      const token = response.data.token;
      if (!token) throw new Error("Login response did not contain a token.");
      // Store JWT securely (localStorage for demo)
      localStorage.setItem("token", token);
      localStorage.setItem("user", JSON.stringify(response.data));
      // Redirect to dashboard
      navigate("/dashboard", { replace: true });
    } catch (err) {
      setError(err.response?.data?.message || "Login failed. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex min-h-[calc(100vh-5rem)] items-center justify-center bg-[#0b1220] p-4">
      <div className="w-full max-w-md rounded-2xl border border-slate-700 bg-[#111a2b] p-8 shadow-2xl shadow-slate-950/30">
        <h2 className="text-2xl font-bold text-gray-800 dark:text-gray-100 mb-6 text-center">
          Welcome back
        </h2>
        {error && (
          <div className="rounded-lg border border-red-400/30 bg-red-500/10 px-4 py-2 text-red-300 mb-4">
            {error}
          </div>
        )}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="mb-1 block text-sm text-slate-300" htmlFor="email">
              Email
            </label>
            <input
              id="email"
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-2 border rounded bg-white text-gray-900 placeholder:text-gray-500 focus:outline-none focus:ring-2 focus:ring-emerald-500"
            />
          </div>
          <div>
            <label className="mb-1 block text-sm text-slate-300" htmlFor="password">
              Password
            </label>
            <input
              id="password"
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-2 border rounded bg-white text-gray-900 placeholder:text-gray-500 focus:outline-none focus:ring-2 focus:ring-emerald-500"
            />
          </div>
          <button
            type="submit"
            className="w-full rounded-lg bg-emerald-600 px-4 py-3 font-semibold text-white transition-colors hover:bg-emerald-500 disabled:opacity-60"
            disabled={isLoading}
          >
            {isLoading ? "Signing in..." : "Sign In"}
          </button>
        </form>
        <p className="mt-4 text-center text-sm text-slate-400">
          Don't have an account?{' '}
          <Link to="/register" className="text-emerald-400 hover:underline">
            Register
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
