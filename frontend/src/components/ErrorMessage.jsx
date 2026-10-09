import React from 'react';
import { AlertCircle } from 'lucide-react';

export default function ErrorMessage({ message }) {
  if (!message) return null;
  
  return (
    <div 
      className="relative overflow-hidden p-4 mb-6 rounded-2xl bg-gradient-to-r from-red-500/10 to-red-500/5 border border-red-500/20 backdrop-blur-md shadow-[0_4px_20px_-4px_rgba(239,68,68,0.15)] animate-in fade-in zoom-in-95 duration-300 ease-out"
      role="alert"
    >
      {/* Decorative side accent */}
      <div className="absolute top-0 left-0 w-1.5 h-full bg-gradient-to-b from-red-500 to-red-600"></div>
      
      <div className="relative flex items-start gap-4 pl-2">
        <div className="flex-shrink-0 bg-red-500/10 p-2 rounded-xl ring-1 ring-red-500/20 shadow-inner">
          <AlertCircle className="w-5 h-5 text-red-400" />
        </div>
        
        <div className="flex-1 pt-0.5">
          <h3 className="text-[15px] font-semibold text-red-200 tracking-wide mb-1">
            Something went wrong
          </h3>
          <p className="text-[14px] text-red-300/90 leading-relaxed font-medium">
            {message}
          </p>
        </div>
      </div>
    </div>
  );
}
