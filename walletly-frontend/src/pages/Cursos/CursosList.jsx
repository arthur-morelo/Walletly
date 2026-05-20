import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import CursoCard from './CursoCard'
import { cursos as cursosMock } from '../../data/data'
import Header from '../../components/Header'
import { getCursos } from '../../services/cursoService'
import { useAuth } from '../../contexts/AuthContext'
import AdminCursosPanel from './AdminCursosPanel'

export default function CursosList() {
  const [cursos, setCursos] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  const fetchCursos = async () => {
    setLoading(true);
    try {
      const response = await getCursos();
      // Se a API retornar dados válidos, usamos. Se não, fallback
      if (response.data && response.data.length > 0) {
        setCursos(response.data);
      } else {
        setCursos(cursosMock); // Fallback caso vazio
      }
    } catch (error) {
      console.warn("API de cursos indisponível. Usando dados locais.", error);
      setCursos(cursosMock); // Fallback caso erro
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCursos();
  }, []);

  const isAdmin = user?.email === 'arthurmorelo@gmail.com';

  return (
    <div className="min-h-screen bg-gray-50">
      <Header/>
      
      <div className="container mx-auto px-4 py-8 max-w-7xl">
        
        {isAdmin && (
          <AdminCursosPanel cursos={cursos} onCursosUpdated={fetchCursos} />
        )}

        <div className="mb-6 flex justify-between items-end">
          <h1 className="text-3xl font-bold text-gray-800">Cursos Disponíveis</h1>
        </div>

        {loading ? (
          <p className="text-gray-500">Carregando cursos...</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 lg:gap-20">
            {cursos.map((curso) => (
              <Link 
                to={`/cursos/${curso.id}`} 
                key={curso.id}
                className="transform transition-transform duration-300 hover:scale-105"
              >
                <CursoCard 
                  Title={curso.Title} 
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