import React from 'react';

export default function MatchScore({ score, size = 'md' }) {
  const numericScore = typeof score === 'number' ? Math.round(score) : 0;
  
  let color = 'from-emerald-500 to-teal-400 border-emerald-500/30 text-emerald-300';
  if (numericScore < 50) {
    color = 'from-amber-500 to-orange-400 border-amber-500/30 text-amber-300';
  } else if (numericScore < 75) {
    color = 'from-cyan-500 to-blue-400 border-cyan-500/30 text-cyan-300';
  }

  const sizes = {
    sm: 'px-2 py-0.5 text-xs',
    md: 'px-3 py-1 text-sm font-semibold',
    lg: 'px-4 py-2 text-base font-bold'
  };

  return (
    <div className={`inline-flex items-center space-x-1.5 rounded-full bg-slate-900/80 border backdrop-blur-md ${color} ${sizes[size]}`}>
      <span>⚡ {numericScore}% Match</span>
    </div>
  );
}
