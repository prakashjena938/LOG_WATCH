import { createContext, useContext, useState, useCallback } from 'react';
import { api } from '../api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [username, setUsername] = useState(() => localStorage.getItem('logfetch_username'));
  const [token, setToken] = useState(() => localStorage.getItem('logfetch_token'));

  const login = useCallback(async (credentials) => {
    const data = await api.login(credentials);
    localStorage.setItem('log_token', data.token);
    localStorage.setItem('logfetch_username', data.username);
    setToken(data.token);
    setUsername(data.username);
    return data;
  }, []);

  const register = useCallback(async (payload) => {
    return api.register(payload);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('logfetch_token');
    localStorage.removeItem('logfetch_username');
    setToken(null);
    setUsername(null);
  }, []);

  const value = {
    username,
    token,
    isAuthenticated: Boolean(token),
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within an AuthProvider');
  return ctx;
}
