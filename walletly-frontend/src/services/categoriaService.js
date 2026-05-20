import api from './api';

export const getCategorias = async () => {
  return await api.get('/api/categorias');
};

export const createCategoria = async (dados) => {
  return await api.post('/api/categorias', dados);
};

export const deleteCategoria = async (id) => {
  return await api.delete(`/api/categorias/${id}`);
};
