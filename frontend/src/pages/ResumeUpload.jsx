import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api';

export default function ResumeUpload() {
  const [file, setFile] = useState(null); const [status, setStatus] = useState(''); const [error, setError] = useState(''); const navigate = useNavigate();
  const submit = async e => { e.preventDefault(); if (!file) return setError('Choose a PDF resume first.'); const data = new FormData(); data.append('file', file); try { await api.post('/resume/upload', data, { headers: { 'Content-Type': 'multipart/form-data' } }); setStatus('Resume uploaded. You can now analyze it.'); } catch (err) { setError(err.response?.data?.message || 'Upload failed.'); } };
  return <div className="mx-auto max-w-xl space-y-6"><h1 className="text-3xl font-bold">Resume workspace</h1><section className="glass-panel rounded-2xl p-6"><form onSubmit={submit} className="space-y-4"><label className="block text-sm text-slate-300">Upload a PDF resume (max 5MB)<input type="file" accept="application/pdf,.pdf" onChange={e => { setFile(e.target.files[0]); setError(''); }} className="mt-2 block w-full rounded-lg border border-slate-600 bg-slate-900 p-3" /></label><button className="rounded-lg bg-cyan-500 px-4 py-2 font-semibold text-slate-950">Upload resume</button></form>{status && <p className="mt-4 text-emerald-400">{status}</p>}{error && <p className="mt-4 text-rose-400">{error}</p>}<div className="mt-6 flex gap-4"><Link to="/resume/analysis" className="text-cyan-400 hover:underline">Analyze resume →</Link><button onClick={() => navigate('/jobs')} className="text-slate-400 hover:text-white">Browse jobs</button></div></section></div>;
}
