import api from './api';

export const getOrcamentos = async () => {
  return await api.get('/api/orcamentos');
};

export const createOrcamento = async (dados) => {
  return await api.post('/api/orcamentos', dados);
};

export const updateOrcamento = async (id, dados) => {
  return await api.put(`/api/orcamentos/${id}`, dados);
};

export const deleteOrcamento = async (id) => {
  return await api.delete(`/api/orcamentos/${id}`);
};
