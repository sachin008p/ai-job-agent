import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import api from "../api";
import { ArrowLeft, ArrowRight, Check, Eye, EyeOff, LockKeyhole, Mail, Sparkles } from "lucide-react";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
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
    <div className="auth-page">
      <div className="auth-visual"><Link to="/" className="auth-back"><ArrowLeft size={16} /> Back to home</Link><div className="auth-visual-content"><span className="auth-spark"><Sparkles size={17} /> AI-powered career search</span><h1>Find work that<br /><i>fits your life.</i></h1><p>One focused workspace for better jobs, smarter matches, and your next opportunity.</p><div className="auth-points"><span><Check /> Verified opportunities</span><span><Check /> Personalized job matches</span><span><Check /> Simple application tracking</span></div></div><div className="auth-visual-orb orb-one" /><div className="auth-visual-orb orb-two" /></div>
      <div className="auth-form-side"><div className="auth-form-wrap"><div className="auth-mobile-brand"><span className="brand-symbol">✦</span> AI Job <em>Agent</em></div><div className="auth-form-heading"><p className="kicker">Welcome back</p><h2>Log in to your workspace</h2><p>Pick up where you left off in your job search.</p></div>
        {error && (
          <div className="auth-error">
            {error}
          </div>
        )}
        <form onSubmit={handleSubmit} className="auth-form">
          <div className="auth-input-group">
            <label htmlFor="email">Email address</label>
            <div className="auth-input"><Mail size={18} /><input id="email" type="email" required value={email} onChange={(e) => setEmail(e.target.value)} placeholder="you@example.com" /></div>
          </div>
          <div className="auth-input-group">
            <div className="auth-label-row"><label htmlFor="password">Password</label><a href="#forgot">Forgot password?</a></div>
            <div className="auth-input"><LockKeyhole size={18} /><input id="password" type={showPassword ? "text" : "password"} required value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Enter your password" /><button type="button" onClick={() => setShowPassword((value) => !value)} aria-label={showPassword ? "Hide password" : "Show password"}>{showPassword ? <EyeOff size={18} /> : <Eye size={18} />}</button></div>
          </div>
          <button type="submit" className="auth-submit" disabled={isLoading}>{isLoading ? "Signing in..." : <>Continue to workspace <ArrowRight size={17} /></>}</button>
        </form>
        <p className="auth-switch">Don't have an account? <Link to="/register">Create one free</Link></p><div className="demo-hint"><span>Demo access</span><small>user@example.com · user123</small></div><p className="auth-legal">By continuing, you agree to our Terms of Use and Privacy Policy.</p></div></div>
    </div>
  );
};

export default Login;
