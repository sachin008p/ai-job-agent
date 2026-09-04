import React, { useState } from 'react';
import { ArrowRight, BriefcaseBusiness, Check, ChevronRight, Heart, Search, ShieldCheck, Sparkles, Star, WandSparkles } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';

const featuredJobs = [
  { title: 'Senior Product Designer', company: 'Northstar Labs', location: 'Remote · Worldwide', type: 'Full-time', salary: '$120k – $160k', tag: '98% match', tone: 'violet' },
  { title: 'Customer Success Manager', company: 'Brightside Health', location: 'Remote · United States', type: 'Full-time', salary: '$85k – $110k', tag: '94% match', tone: 'orange' },
  { title: 'Content Marketing Specialist', company: 'Orbit Commerce', location: 'Hybrid · New York', type: 'Part-time', salary: '$55k – $75k', tag: '91% match', tone: 'blue' },
];

const categories = ['Design', 'Software Development', 'Marketing', 'Customer Support', 'Data & Analytics', 'Project Management'];

const Home = () => {
  const navigate = useNavigate();
  const [query, setQuery] = useState('');
  const [location, setLocation] = useState('');
  const [saved, setSaved] = useState([]);

  const search = (event) => {
    event.preventDefault();
    navigate(`/jobs${query || location ? `?q=${encodeURIComponent(query)}&location=${encodeURIComponent(location)}` : ''}`);
  };

  const toggleSaved = (title) => setSaved((items) => items.includes(title) ? items.filter((item) => item !== title) : [...items, title]);

  return (
    <div className="home-page">
      <section className="home-hero">
        <div className="home-container hero-grid">
          <div className="hero-copy">
            <div className="eyebrow"><Sparkles size={15} /> Your smarter job search starts here</div>
            <h1>Find work that fits <span>your life.</span></h1>
            <p className="hero-lede">Discover legitimate remote and flexible jobs matched to your skills, goals, and the way you want to work.</p>
            <form className="search-box" onSubmit={search}>
              <div className="search-field"><Search size={20} /><input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Job title, skill, or keyword" aria-label="Job title, skill, or keyword" /></div>
              <div className="search-field location-field"><BriefcaseBusiness size={20} /><input value={location} onChange={(e) => setLocation(e.target.value)} placeholder="Location or Remote" aria-label="Location or Remote" /></div>
              <button className="primary-button search-button" type="submit">Search jobs <ArrowRight size={18} /></button>
            </form>
            <p className="search-note"><ShieldCheck size={16} /> Every listing is screened by our team and AI.</p>
          </div>
          <div className="hero-art" aria-label="A flexible work day illustration">
            <div className="art-sun" /><div className="art-card art-card-top"><div className="mini-avatar avatar-purple">N</div><div><b>New match found</b><small>98% skill fit · just now</small></div><Check size={18} /></div>
            <div className="art-window"><div className="window-bar"><i /><i /><i /></div><div className="window-content"><div className="mock-line long" /><div className="mock-line" /><div className="mock-pills"><span /><span /><span /></div><div className="mock-line short" /><div className="mock-line medium" /></div></div>
            <div className="art-card art-card-bottom"><WandSparkles size={22} /><div><b>AI-powered matching</b><small>Roles picked for you</small></div></div>
          </div>
        </div>
        <div className="hero-curve" />
      </section>

      <section className="trusted-row"><div className="home-container"><span>Trusted by job seekers building their next chapter</span><div className="logo-cloud"><b>northstar</b><b>luma</b><b>orbit</b><b>BRIGHTSIDE</b><b>vertex</b></div></div></section>

      <section id="how-it-works" className="section-padding"><div className="home-container"><div className="section-heading"><div><p className="kicker">A better way to search</p><h2>More than a job board.</h2></div><p>Everything you need to move from “looking” to “landed” with confidence.</p></div><div className="benefit-grid">
        <div className="benefit-card"><div className="benefit-icon mint"><ShieldCheck /></div><h3>Real jobs, verified</h3><p>No ads, scams, or endless scrolling. We screen every listing so you can search with confidence.</p><Link to="/jobs">Explore verified jobs <ChevronRight size={16} /></Link></div>
        <div className="benefit-card featured-benefit"><div className="benefit-icon yellow"><Sparkles /></div><h3>Matched by AI</h3><p>Upload your resume and get a clear match score for roles that actually fit your experience.</p><Link to="/resume/upload">Analyze my resume <ChevronRight size={16} /></Link></div>
        <div className="benefit-card"><div className="benefit-icon lavender"><BriefcaseBusiness /></div><h3>Stay organized</h3><p>Keep your saved jobs and applications in one calm, focused workspace.</p><Link to="/register">Create your workspace <ChevronRight size={16} /></Link></div>
      </div></div></section>

      <section id="jobs" className="jobs-section"><div className="home-container"><div className="section-heading jobs-heading"><div><p className="kicker">Fresh opportunities</p><h2>Jobs picked for possibility.</h2></div><Link className="text-link" to="/jobs">View all jobs <ArrowRight size={17} /></Link></div><div className="job-grid">{featuredJobs.map((job) => <article className="job-card" key={job.title}><div className="job-card-top"><div className={`company-mark ${job.tone}`}>{job.company[0]}</div><button className={`save-button ${saved.includes(job.title) ? 'is-saved' : ''}`} onClick={() => toggleSaved(job.title)} aria-label={`Save ${job.title}`}><Heart size={18} fill={saved.includes(job.title) ? 'currentColor' : 'none'} /></button></div><h3>{job.title}</h3><p className="company-name">{job.company}</p><div className="job-meta"><span>{job.location}</span><span>{job.type}</span></div><div className="job-card-footer"><b>{job.salary}</b><span className="match-pill">{job.tag}</span></div></article>)}</div><div className="center-cta"><Link to="/jobs" className="secondary-button">Find your next role <ArrowRight size={17} /></Link></div></div></section>

      <section className="story-section"><div className="home-container story-grid"><div className="story-art"><div className="story-photo"><div className="photo-shape one" /><div className="photo-shape two" /><div className="photo-person" /></div><div className="floating-quote">“I found a role that gives me room to do my best work.” <small>— Maya, Product Designer</small></div></div><div className="story-copy"><p className="kicker">Built around you</p><h2>Your career. Your rules.</h2><p>Whether you want fully remote, flexible hours, or a role where you can grow, AI Job Agent brings the signal to the surface.</p><div className="check-list"><span><Check /> Personalized recommendations</span><span><Check /> Resume insights that make sense</span><span><Check /> A simple, focused application tracker</span></div><Link to="/register" className="primary-button">Get started free <ArrowRight size={17} /></Link></div></div></section>

      <section id="categories" className="category-section"><div className="home-container"><div className="section-heading"><div><p className="kicker">Find your direction</p><h2>Explore by category.</h2></div><p>Start with what you know. End up somewhere you love.</p></div><div className="category-grid">{categories.map((category) => <Link to={`/jobs?category=${encodeURIComponent(category)}`} key={category}>{category}<ArrowRight size={17} /></Link>)}</div></div></section>
      <section className="final-cta"><div className="home-container"><p className="kicker">The next opportunity is closer than you think</p><h2>Ready to find work<br />that works for you?</h2><Link to="/register" className="light-button">Create your free account <ArrowRight size={17} /></Link></div></section>
    </div>
  );
};

export default Home;
