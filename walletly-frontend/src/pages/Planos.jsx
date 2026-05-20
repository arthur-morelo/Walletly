import React from 'react';
import Header from '../components/Header';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function Planos() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const handleAssinar = () => {
    // Simular chamada para assinar plano ou redirecionar pro checkout
    alert('Redirecionando para o checkout (simulação)...');
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <Header />
      
      <div className="flex-1 flex flex-col items-center justify-center p-6">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-extrabold text-gray-900 mb-4">Escolha seu Plano</h1>
          <p className="text-xl text-gray-600 max-w-2xl mx-auto">
            Acesse as melhores ferramentas de controle financeiro e desbloqueie todo o seu potencial com nossos cursos.
          </p>
        </div>

        <div className="flex flex-col md:flex-row gap-8 max-w-5xl w-full justify-center">
          
          {/* Card Gratuito */}
          <div className="bg-white rounded-2xl shadow-lg border border-gray-200 p-8 flex-1 flex flex-col hover:shadow-xl transition-shadow">
            <h2 className="text-2xl font-bold text-gray-800 mb-2">Plano Gratuito</h2>
            <p className="text-gray-500 mb-6 h-12">Para quem está começando a organizar as finanças.</p>
            <div className="text-4xl font-extrabold text-gray-900 mb-8">
              R$ 0<span className="text-lg font-medium text-gray-500">/mês</span>
            </div>
            
            <ul className="space-y-4 mb-8 flex-1">
              <li className="flex items-center text-gray-700">
                <svg className="w-5 h-5 text-green-500 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
                Acesso à tela de Metas
              </li>
              <li className="flex items-center text-gray-700">
                <svg className="w-5 h-5 text-green-500 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
                Dashboard completo com Recharts
              </li>
              <li className="flex items-center text-gray-700">
                <svg className="w-5 h-5 text-green-500 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
                Importação de Extrato Bancário
              </li>
              <li className="flex items-center text-gray-400">
                <svg className="w-5 h-5 text-gray-300 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12"></path></svg>
                Área de Cursos Exclusivos
              </li>
            </ul>

            <button 
              disabled={user?.plano === 'free'}
              className="w-full py-3 px-6 rounded-lg font-bold text-white bg-gray-400 cursor-not-allowed"
            >
              Seu Plano Atual
            </button>
          </div>

          {/* Card Pago */}
          <div className="bg-blue-600 rounded-2xl shadow-2xl border border-blue-500 p-8 flex-1 flex flex-col transform md:-translate-y-4 relative">
            <div className="absolute top-0 right-0 bg-yellow-400 text-yellow-900 text-xs font-bold px-3 py-1 rounded-bl-lg rounded-tr-xl uppercase tracking-wider">
              Recomendado
            </div>
            <h2 className="text-2xl font-bold text-white mb-2">Plano Premium</h2>
            <p className="text-blue-100 mb-6 h-12">Para quem quer evoluir financeiramente com conhecimento.</p>
            <div className="text-4xl font-extrabold text-white mb-8">
              R$ 29,90<span className="text-lg font-medium text-blue-200">/mês</span>
            </div>
            
            <ul className="space-y-4 mb-8 flex-1">
              <li className="flex items-center text-blue-50">
                <svg className="w-5 h-5 text-blue-300 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
                Acesso à tela de Metas
              </li>
              <li className="flex items-center text-blue-50">
                <svg className="w-5 h-5 text-blue-300 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
                Dashboard completo com Recharts
              </li>
              <li className="flex items-center text-blue-50">
                <svg className="w-5 h-5 text-blue-300 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
                Importação de Extrato Bancário
              </li>
              <li className="flex items-center text-white font-bold">
                <svg className="w-5 h-5 text-yellow-400 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
                Acesso Exclusivo à Área de Cursos
              </li>
            </ul>

            {user?.plano === 'paid' ? (
               <button 
               className="w-full py-3 px-6 rounded-lg font-bold text-blue-600 bg-white hover:bg-gray-50 transition-colors shadow-md"
               onClick={() => navigate('/cursos')}
             >
               Acessar Cursos
             </button>
            ) : (
              <button 
                onClick={handleAssinar}
                className="w-full py-3 px-6 rounded-lg font-bold text-blue-900 bg-yellow-400 hover:bg-yellow-300 transition-colors shadow-md"
              >
                Assinar Agora
              </button>
            )}
          </div>

        </div>
      </div>
    </div>
  );
}
