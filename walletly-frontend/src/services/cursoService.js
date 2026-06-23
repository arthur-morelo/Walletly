import api from './api';

export const getCursos = async () => {
  return await api.get('/courses');
};

export const getCursoById = async (id) => {
  return await api.get(`/courses/${id}`);
};

export const createCurso = async (curso) => {
  return await api.post('/courses', curso);
};

export const updateCurso = async (id, curso) => {
  return await api.put(`/courses/${id}`, curso);
};

export const deleteCurso = async (id) => {
  return await api.delete(`/courses/${id}`);
};
