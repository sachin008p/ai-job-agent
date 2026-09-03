import React, { useEffect, useState } from 'react';
import api from '../api';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import { Link } from 'react-router-dom';

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // Simple endpoint could be /api/jobs/count etc.; using placeholder data for now
    const fetchStats = async () => {
      try {
        const jobsRes = await api.get('/jobs');
        const appsRes = await api.get('/applications/my');
        setStats({
          totalJobs: jobsRes.data.totalElements ?? jobsRes.data.content?.length ?? jobsRes.data.length ?? 0,
          myApplications: appsRes.data.length,
        });
      } catch (err) {
        setError('Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage message={error} />;

  return (
    <div className="space-y-6">
      <section className="mb-8 grid items-center gap-8 rounded-2xl border border-slate-700 bg-[#111a2b] p-6 md:grid-cols-[1fr_280px] md:p-8">
        <div><p className="mb-2 text-sm font-semibold uppercase tracking-widest text-emerald-400">Career workspace</p><h1 className="text-3xl font-bold">Build your next opportunity</h1><p className="mt-3 max-w-xl text-slate-400">Search relevant roles, understand your skill fit and keep every application organised in one place.</p><Link to="/jobs" className="mt-5 inline-block rounded-lg bg-emerald-600 px-4 py-2 font-semibold text-white hover:bg-emerald-500">Explore jobs</Link></div>
        <img src="/career-hero.svg" alt="Career dashboard overview" className="hidden w-full md:block" />
      </section>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-slate-800 p-4 rounded">
          <h2 className="text-xl font-semibold mb-2">Total Jobs</h2>
          <p className="text-2xl">{stats.totalJobs}</p>
          <Link to="/jobs" className="text-emerald-400 hover:underline mt-2 block">View Jobs</Link>
        </div>
        <div className="bg-slate-800 p-4 rounded">
          <h2 className="text-xl font-semibold mb-2">My Applications</h2>
          <p className="text-2xl">{stats.myApplications}</p>
          <Link to="/applications" className="text-emerald-400 hover:underline mt-2 block">View Applications</Link>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
