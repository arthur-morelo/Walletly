import React, { useState } from 'react';
import { createCurso, updateCurso, deleteCurso } from '../../services/cursoService';

export default function AdminCursosPanel({ cursos, onCursosUpdated }) {
  const [isEditing, setIsEditing] = useState(false);
  const [currentCurso, setCurrentCurso] = useState(null);
  
  const [formData, setFormData] = useState({
    Title: '',
    courseDescription: '',
    textLeft: '',
    textRight: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isEditing) {
        await updateCurso(currentCurso.id, formData);
        alert('Curso atualizado com sucesso!');
      } else {
        await createCurso(formData);
        alert('Curso criado com sucesso!');
      }
      setFormData({ Title: '', courseDescription: '', textLeft: '', textRight: '' });
      setIsEditing(false);
      setCurrentCurso(null);
      if (onCursosUpdated) onCursosUpdated();
    } catch (error) {
      console.error('Erro ao salvar curso:', error);
      alert('Houve um erro, talvez a API não esteja pronta. Verifique o console.');
    }
  };

  const handleEdit = (curso) => {
    setIsEditing(true);
    setCurrentCurso(curso);
    setFormData({
      Title: curso.Title || '',
      courseDescription: curso.courseDescription || '',
      textLeft: curso.textLeft || '',
      textRight: curso.textRight || ''
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este curso?')) {
      try {
        await deleteCurso(id);
        alert('Curso excluído!');
        if (onCursosUpdated) onCursosUpdated();
      } catch (error) {
        console.error('Erro ao excluir curso:', error);
        alert('Houve um erro ao excluir. Verifique o console.');
      }
    }
  };

  const handleCancel = () => {
    setIsEditing(false);
    setCurrentCurso(null);
    setFormData({ Title: '', courseDescription: '', textLeft: '', textRight: '' });
  };

  return (
    <div className="bg-white rounded-xl shadow-lg p-6 border border-blue-200 mb-8 mt-4">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Painel Administrativo de Cursos</h2>
      
      <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8 bg-gray-50 p-4 rounded-lg">
        <div className="col-span-1 md:col-span-2">
          <label className="block text-sm font-medium text-gray-700 mb-1">Título do Curso / Módulo</label>
          <input 
            type="text" 
            name="Title" 
            value={formData.Title} 
            onChange={handleInputChange} 
            required
            className="w-full p-2 border border-gray-300 rounded-md" 
          />
        </div>
        
        <div className="col-span-1 md:col-span-2">
          <label className="block text-sm font-medium text-gray-700 mb-1">Descrição Curta</label>
          <input 
            type="text" 
            name="courseDescription" 
            value={formData.courseDescription} 
            onChange={handleInputChange} 
            required
            className="w-full p-2 border border-gray-300 rounded-md" 
          />
        </div>

        <div className="col-span-1">
          <label className="block text-sm font-medium text-gray-700 mb-1">Conteúdo (Lado Esquerdo) / Video Link</label>
          <textarea 
            name="textLeft" 
            value={formData.textLeft} 
            onChange={handleInputChange} 
            rows="4"
            className="w-full p-2 border border-gray-300 rounded-md" 
          ></textarea>
        </div>

        <div className="col-span-1">
          <label className="block text-sm font-medium text-gray-700 mb-1">Conteúdo (Lado Direito) / Resumo</label>
          <textarea 
            name="textRight" 
            value={formData.textRight} 
            onChange={handleInputChange} 
            rows="4"
            className="w-full p-2 border border-gray-300 rounded-md" 
          ></textarea>
        </div>

        <div className="col-span-1 md:col-span-2 flex justify-end gap-2 mt-2">
          {isEditing && (
            <button type="button" onClick={handleCancel} className="bg-gray-400 hover:bg-gray-500 text-white font-bold py-2 px-4 rounded">
              Cancelar
            </button>
          )}
          <button type="submit" className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            {isEditing ? 'Atualizar Curso' : 'Criar Curso'}
          </button>
        </div>
      </form>

      <div>
        <h3 className="text-xl font-bold text-gray-800 mb-4">Cursos Existentes (Edição Rápida)</h3>
        <div className="flex flex-col gap-2">
          {cursos.map(curso => (
            <div key={curso.id} className="flex items-center justify-between bg-gray-100 p-3 rounded">
              <span className="font-medium text-gray-800">{curso.Title}</span>
              <div className="flex gap-2">
                <button onClick={() => handleEdit(curso)} className="text-sm bg-yellow-500 hover:bg-yellow-600 text-white py-1 px-3 rounded">Editar</button>
                <button onClick={() => handleDelete(curso.id)} className="text-sm bg-red-500 hover:bg-red-600 text-white py-1 px-3 rounded">Excluir</button>
              </div>
            </div>
          ))}
          {cursos.length === 0 && <p className="text-gray-500">Nenhum curso encontrado.</p>}
        </div>
      </div>
    </div>
  );
}
