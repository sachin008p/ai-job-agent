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
    <header className={`site-header ${token ? 'app-header' : ''}`}>
      <Link to="/" className="brand-lockup">
        <span className="brand-symbol">✦</span>
        <span>AI Job <em>Agent</em></span>
      </Link>
      <nav className="site-nav">
        {!token && <><a href="#how-it-works">How it works</a><a href="#jobs">Explore jobs</a><a href="#categories">Career resources</a><Link to="/login" className="nav-login">Log in</Link><Link to="/register" className="nav-signup">Get started</Link></>}
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
