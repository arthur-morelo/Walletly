import { createContext, useState, useEffect, useContext } from 'react';
import { login as loginService, logout as logoutService } from '../services/authService';

export const AuthContext = createContext();

export const useAuth = () => {
  return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Tenta carregar o usuário do localStorage ao iniciar
    const storedUser = localStorage.getItem('@Walletly:user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  const signIn = async (email, password) => {
    try {
      const response = await loginService({ email, password });
      
      const userData = response.data?.user;
      
      if (userData) {
        setUser(userData);
        localStorage.setItem('@Walletly:user', JSON.stringify(userData));
      }
      
      if (response.data?.token) {
        localStorage.setItem('@Walletly:token', response.data.token);
      }

      return userData;
    } catch (error) {
      throw error;
    }
  };

  const signOut = async () => {
    try {
      await logoutService();
    } catch (e) {
      console.error('Erro ao fazer logout na API', e);
    } finally {
      setUser(null);
      localStorage.removeItem('@Walletly:user');
      localStorage.removeItem('@Walletly:token');
    }
  };

  return (
    <AuthContext.Provider value={{ user, signed: !!user, loading, signIn, signOut }}>
      {children}
    </AuthContext.Provider>
  );
};
