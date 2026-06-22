import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import CursoCard from './CursoCard';
import Header from '../../components/Header';
import api from '../../services/api';

export default function CursosList() {
  const [cursos, setCursos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCursos = async () => {
      try {
        const response = await api.get("/cursos");
        setCursos(response.data);
      } catch (error) {
        console.error("Erro ao buscar cursos:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchCursos();
  }, []);

  return (
    <div className="min-h-screen bg-gray-50">
      <Header/>
      
      <div className="container mx-auto px-4 py-8 max-w-7xl">
        {loading ? (
           <p className="text-center text-gray-500">Carregando cursos...</p>
        ) : cursos.length === 0 ? (
           <p className="text-center text-gray-500">Nenhum curso cadastrado.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 lg:gap-20">
            {cursos.map((curso) => (
              <Link 
                to={`/cursos/${curso.id}`} 
                key={curso.id}
                className="transform transition-transform duration-300 hover:scale-105"
              >
                <CursoCard 
                  Title={curso.title} 
                  courseDescription={curso.courseDescription}
                />
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}