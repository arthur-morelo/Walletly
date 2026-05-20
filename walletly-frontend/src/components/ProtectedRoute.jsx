import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export default function ProtectedRoute({ children }) {
  const { signed, loading } = useAuth();

  if (loading) {
    return <div className="flex h-screen items-center justify-center text-xl">Carregando...</div>;
  }

  if (!signed) {
    return <Navigate to="/login" replace />;
  }

  return children;
}
