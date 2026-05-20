import api from './api';

export const getCursos = async () => {
  return await api.get('/cursos');
};

export const getCursoById = async (id) => {
  return await api.get(`/cursos/${id}`);
};

export const createCurso = async (curso) => {
  return await api.post('/cursos', curso);
};

export const updateCurso = async (id, curso) => {
  return await api.put(`/cursos/${id}`, curso);
};

export const deleteCurso = async (id) => {
  return await api.delete(`/cursos/${id}`);
};
