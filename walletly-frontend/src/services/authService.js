import api from './api';

export const login = async (credenciais) => {
  return await api.post('/auth/login', credenciais);
};

export const register = async (dadosUsuario) => {
  return await api.post('/auth/register', dadosUsuario);
};

export const logout = async () => {
  return await api.post('/auth/logout');
};
