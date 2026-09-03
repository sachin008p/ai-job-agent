import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import JobSearch from './pages/JobSearch';
import JobDetails from './pages/JobDetails';
import ResumeUpload from './pages/ResumeUpload';
import ResumeAnalysis from './pages/ResumeAnalysis';
import JobRecommendations from './pages/JobRecommendations';
import MyApplications from './pages/MyApplications';
import AiChat from './pages/AiChat';
import Header from './components/Header';
import ProtectedRoute from './components/ProtectedRoute';

const App = () => {
  const isAuthenticated = !!localStorage.getItem('token');

  return (
    <div className="min-h-screen flex flex-col bg-[#0b1220] text-slate-100">
      <Header />
      <main className="flex-1 p-6 md:p-8">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          {/* Protected routes */}
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute isAuth={isAuthenticated}>
                <Dashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/jobs"
            element={
              <ProtectedRoute isAuth={isAuthenticated}>
                <JobSearch />
              </ProtectedRoute>
            }
          />
          <Route
            path="/jobs/:id"
            element={
              <ProtectedRoute isAuth={isAuthenticated}>
                <JobDetails />
              </ProtectedRoute>
            }
          />
          <Route
            path="/resume/upload"
            element={
              <ProtectedRoute isAuth={isAuthenticated}>
                <ResumeUpload />
              </ProtectedRoute>
            }
          />
          <Route
            path="/resume/analysis"
            element={
              <ProtectedRoute isAuth={isAuthenticated}>
                <ResumeAnalysis />
              </ProtectedRoute>
            }
          />
          <Route
            path="/recommendations"
            element={
              <ProtectedRoute isAuth={isAuthenticated}>
                <JobRecommendations />
              </ProtectedRoute>
            }
          />
          <Route
            path="/applications"
            element={
              <ProtectedRoute isAuth={isAuthenticated}>
                <MyApplications />
              </ProtectedRoute>
            }
          />
          <Route
            path="/ai-chat"
            element={
              <ProtectedRoute isAuth={isAuthenticated}>
                <AiChat />
              </ProtectedRoute>
            }
          />
          <Route path="/" element={<Navigate to={isAuthenticated ? '/dashboard' : '/login'} replace />} />
        </Routes>
      </main>
    </div>
  );
};

export default App;
