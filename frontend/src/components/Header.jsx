import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

const Header = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigate('/login', { replace: true });
  };

  return (
    <header className="border-b border-slate-700/70 bg-[#111a2b] px-6 py-4 text-slate-100 shadow-lg shadow-slate-950/10">
      <Link to="/" className="flex items-center gap-3 text-xl font-bold tracking-tight">
        <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-emerald-600 text-sm">AJ</span>
        <span>AI Job Agent</span>
      </Link>
      <nav className="flex flex-wrap items-center justify-end gap-x-5 gap-y-2 text-sm font-medium text-slate-300">
        {token && (
          <>
            <Link to="/dashboard" className="transition-colors hover:text-white">Dashboard</Link>
            <Link to="/jobs" className="transition-colors hover:text-white">Jobs</Link>
            <Link to="/resume/upload" className="transition-colors hover:text-white">Resume</Link>
            <Link to="/recommendations" className="transition-colors hover:text-white">Recommendations</Link>
            <Link to="/applications" className="transition-colors hover:text-white">Applications</Link>
            <Link to="/ai-chat" className="transition-colors hover:text-white">Assistant</Link>
            <button
              onClick={handleLogout}
              className="rounded-lg border border-slate-600 px-3 py-2 text-slate-200 transition hover:border-red-400 hover:text-red-300"
            >
              Logout
            </button>
          </>
        )}
      </nav>
    </header>
  );
};

export default Header;
