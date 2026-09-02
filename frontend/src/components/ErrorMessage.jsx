import React from 'react';
import { AlertCircle } from 'lucide-react';

export default function ErrorMessage({ message }) {
  if (!message) return null;
  return (
    <div className="p-4 mb-4 rounded-xl bg-red-500/10 border border-red-500/30 text-red-300 flex items-start space-x-3 text-sm">
      <AlertCircle className="w-5 h-5 shrink-0 mt-0.5 text-red-400" />
      <div>{message}</div>
    </div>
  );
}
