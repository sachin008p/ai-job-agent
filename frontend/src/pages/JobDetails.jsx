import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import api from '../api';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import MatchScore from '../components/MatchScore';

export default function JobDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [job, setJob] = useState(null);
  const [match, setMatch] = useState(null);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  useEffect(() => { api.get(`/jobs/${id}`).then(r => setJob(r.data)).catch(() => setError('Job not found.')); }, [id]);
  const apply = async () => {
    try { await api.post('/applications', { jobId: Number(id) }); setMessage('Application submitted successfully.'); }
    catch (e) { setError(e.response?.data?.message || 'Could not submit application.'); }
  };
  const calculateMatch = async () => {
    try { const r = await api.post(`/jobs/${id}/match`); setMatch(r.data); }
    catch (e) { setError(e.response?.data?.message || 'Could not calculate your match.'); }
  };
  if (error && !job) return <ErrorMessage message={error} />;
  if (!job) return <LoadingSpinner />;
  return <div className="max-w-4xl mx-auto space-y-6">
    <Link to="/jobs" className="text-emerald-400 hover:underline">← Back to jobs</Link>
    <section className="glass-panel rounded-2xl p-6 space-y-4">
      <div><p className="text-emerald-400">{job.company} · {job.location}</p><h1 className="text-3xl font-bold">{job.title}</h1></div>
      <div className="flex flex-wrap gap-2 text-sm"><span className="rounded-full bg-slate-700 px-3 py-1">{job.jobType || 'Full-time'}</span><span className="rounded-full bg-slate-700 px-3 py-1">{job.experience || 'Experience not specified'}</span><span className="rounded-full bg-slate-700 px-3 py-1">{job.salary || 'Salary not disclosed'}</span></div>
      <div><h2 className="text-lg font-semibold">About the role</h2><p className="mt-2 whitespace-pre-wrap text-slate-300">{job.description || 'No description provided.'}</p></div>
      <div><h2 className="text-lg font-semibold">Technology and skills</h2><p className="mt-2 text-slate-300">{job.requiredSkills || job.technology}</p></div>
      <div className="flex flex-wrap gap-3"><button onClick={apply} className="rounded-lg bg-emerald-600 px-4 py-2 font-semibold text-white hover:bg-emerald-500">Apply now</button><button onClick={calculateMatch} className="rounded-lg border border-emerald-400 px-4 py-2 text-emerald-300 hover:bg-emerald-400/10">Calculate my match</button></div>
      {message && <p className="text-emerald-400">{message}</p>}{error && <ErrorMessage message={error} />}
    </section>
    {match && <section className="glass-card rounded-2xl p-6 space-y-4"><div className="flex items-center justify-between"><h2 className="text-xl font-semibold">Your match</h2><MatchScore score={match.matchPercentage} size="lg" /></div><p className="text-slate-300">{match.explanation}</p><div className="grid gap-4 md:grid-cols-2"><div><h3 className="font-semibold text-emerald-300">Matched skills</h3><p className="text-slate-300">{match.matchedSkills?.join(', ') || 'None identified'}</p></div><div><h3 className="font-semibold text-amber-300">Skills to develop</h3><p className="text-slate-300">{match.missingSkills?.join(', ') || 'None identified'}</p></div></div></section>}
  </div>;
}
