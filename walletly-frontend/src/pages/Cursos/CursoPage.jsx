import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Header from "../../components/Header";
import CursoNotFound from "./CursoNotFound";
import api from "../../services/api";

export default function Curso() {
  const { id } = useParams();
  const [curso, setCurso] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCurso = async () => {
      try {
        const response = await api.get(`/cursos/${id}`);
        setCurso(response.data);
      } catch (error) {
        console.error("Erro ao buscar curso:", error);
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
        <div className="container mx-auto px-4 py-12 text-center text-gray-500">
          Carregando curso...
        </div>
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

  const { title, textLeft, textRight } = curso;

  return (
    <>
      <Header />
      <div className="container mx-auto px-4 py-12 max-w-7xl">
        {/* Cabeçalho com linha decorativa */}
        <div className="flex items-center justify-center flex-col mb-16">
          <h1 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4 text-center">
            {title}
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
