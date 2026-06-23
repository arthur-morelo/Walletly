import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Header from "../../components/Header";
import { cursos as cursosMock } from "../../data/data";
import CursoNotFound from "./CursoNotFound";
import { getCursoById } from "../../services/cursoService";

export default function Curso() {
  const { id } = useParams();
  const [curso, setCurso] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCurso = async () => {
      try {
        const response = await getCursoById(id);
        if (response.data) {
          setCurso(response.data);
        } else {
          // Fallback para mock local caso a API falhe mas não dispare catch
          const local = cursosMock.find((c) => c.id === parseInt(id));
          setCurso(local);
        }
      } catch (error) {
        console.warn("Falha ao buscar curso da API, usando mock local.");
        const local = cursosMock.find((c) => c.id === parseInt(id));
        setCurso(local);
      } finally {
        setLoading(false);
      }
    };
    fetchCurso();
  }, [id]);

  if (loading) {
    return (
      <>
        <Header />
        <div className="container mx-auto px-4 py-12 text-center text-xl">Carregando curso...</div>
      </>
    );
  }

  if (!curso) {
    return (
      <>
        <Header />
        <CursoNotFound />
      </>
    );
  }

  let Title = curso.Title || curso.title;
  let textLeft = curso.textLeft;
  let textRight = curso.textRight;
  if (!textLeft && curso.description) {
      try {
          const parsed = JSON.parse(curso.description);
          textLeft = parsed.textLeft;
          textRight = parsed.textRight;
      } catch(e) {
          textLeft = curso.description;
      }
  }

  return (
    <>
      <Header />
      <div className="container mx-auto px-4 py-12 max-w-7xl">
        {/* Cabeçalho com linha decorativa */}
        <div className="flex items-center justify-center flex-col mb-16">
          <h1 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4 text-center">
            {Title}
          </h1>
          <div className="w-24 h-1 bg-blue-600 rounded-full"></div>
        </div>

        {/* Conteúdo em duas colunas */}
        <div className="flex flex-col lg:flex-row gap-8 lg:gap-12 items-start">
          {/* Coluna esquerda */}
          <div className="w-full lg:w-1/2">
            <div className="bg-white rounded-xl shadow-lg p-8 hover:shadow-xl transition-shadow duration-300 border border-gray-100">
              <p className="text-gray-700 leading-relaxed text-lg whitespace-pre-line">
                {textLeft}
              </p>
            </div>
          </div>

          {/* Coluna direita */}
          <div className="w-full lg:w-1/2">
            <div className="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl shadow-lg p-8 hover:shadow-xl transition-shadow duration-300 border border-blue-100">
              <div className="text-gray-700 leading-relaxed text-lg whitespace-pre-line">
                {textRight}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
