import api from './api';

export const getContas = async () => {
  return await api.get('/api/contas');
};

export const createConta = async (dados) => {
  return await api.post('/api/contas', dados);
};

export const updateConta = async (id, dados) => {
  return await api.put(`/api/contas/${id}`, dados);
};

export const deleteConta = async (id) => {
  return await api.delete(`/api/contas/${id}`);
};
