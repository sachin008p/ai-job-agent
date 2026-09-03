import React, { useEffect, useState } from 'react';
import api from '../api';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import MatchScore from '../components/MatchScore';
import { Link } from 'react-router-dom';

const JobSearch = () => {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [totalPages, setTotalPages] = useState(1);
  const [filters, setFilters] = useState({ title: '', location: '', skills: '' });
  const [activeFilters, setActiveFilters] = useState({ title: '', location: '', skills: '' });
  const [page, setPage] = useState(0);
  const size = 10;

  const fetchJobs = async (pageNum) => {
    try {
      const res = await api.get('/jobs', { params: { page: pageNum, size, ...activeFilters } });
      setJobs(res.data.content ?? res.data);
      setTotalPages(res.data.totalPages ?? 1);
    } catch (err) {
      setError('Failed to load jobs');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchJobs(page);
  }, [page, activeFilters]);

  const submitSearch = (event) => {
    event.preventDefault();
    setPage(0);
    setActiveFilters(filters);
  };

  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage message={error} />;

  return (
    <div className="space-y-4">
      <h1 className="text-2xl font-bold">Job Search</h1>
      <form onSubmit={submitSearch} className="glass-panel grid gap-3 rounded-xl p-4 md:grid-cols-[1fr_1fr_1fr_auto]">
        <input value={filters.title} onChange={(e) => setFilters({ ...filters, title: e.target.value })} placeholder="Role or title" className="rounded-lg border border-slate-600 bg-slate-900 px-3 py-2" />
        <input value={filters.location} onChange={(e) => setFilters({ ...filters, location: e.target.value })} placeholder="Location" className="rounded-lg border border-slate-600 bg-slate-900 px-3 py-2" />
        <input value={filters.skills} onChange={(e) => setFilters({ ...filters, skills: e.target.value })} placeholder="Technology or skill" className="rounded-lg border border-slate-600 bg-slate-900 px-3 py-2" />
        <button className="rounded-lg bg-emerald-600 px-4 py-2 font-semibold text-white hover:bg-emerald-500">Search</button>
      </form>
      <div className="grid md:grid-cols-2 gap-4">
        {jobs.map((job) => (
          <div key={job.id} className="glass-card p-4 rounded-xl transition-shadow">
            <h2 className="text-xl font-semibold">{job.title}</h2>
            <p className="text-sm text-gray-400">{job.company} · {job.location}</p>
            <p className="mt-2 line-clamp-3">{job.description}</p>
            <p className="mt-2 text-sm text-emerald-300">{job.salary} · {job.jobType}</p>
            <MatchScore score={job.matchScore ?? 0} />
            <Link to={`/jobs/${job.id}`} className="text-emerald-400 hover:underline mt-2 inline-block">
              View details →
            </Link>
          </div>
        ))}
      </div>
      {jobs.length === 0 && <p className="rounded-xl border border-slate-700 p-6 text-center text-slate-400">No jobs match your search.</p>}
      <div className="flex justify-between mt-4">
        <button
          disabled={page === 0}
          onClick={() => setPage(page - 1)}
          className="bg-gray-600 hover:bg-gray-700 text-white py-1 px-3 rounded"
        >
          Previous
        </button>
        <button
          disabled={page + 1 >= totalPages}
          onClick={() => setPage(page + 1)}
          className="bg-gray-600 hover:bg-gray-700 text-white py-1 px-3 rounded"
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default JobSearch;
