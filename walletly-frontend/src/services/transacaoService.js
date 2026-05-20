import api from './api';

export const getTransacoes = async (filtros) => {
  return await api.get('/api/transacoes/filter', { params: filtros });
};

export const createTransacao = async (dados) => {
  return await api.post('/api/transacoes', dados);
};

export const deleteTransacao = async (id) => {
  return await api.delete(`/api/transacoes/${id}`);
};
