import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export default function PlanRoute({ children }) {
  const { user, loading } = useAuth();

  if (loading) {
    return <div className="flex h-screen items-center justify-center text-xl">Carregando...</div>;
  }

  // Se não estiver logado, manda para o login (embora ProtectedRoute já deva cobrir isso)
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // Verifica plano pago ou admin
  if (user.plano !== 'paid' && user.email !== 'arthurmorelo@gmail.com') {
    return <Navigate to="/planos" replace />;
  }

  return children;
}
