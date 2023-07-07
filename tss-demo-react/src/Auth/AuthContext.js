import React, { createContext, useState } from 'react';

// AuthContextの作成
export const AuthContext = createContext();

// AuthProviderコンポーネント
export const AuthProvider = ({ children }) => {
  // ユーザーの認証トークンを管理するstate
  const [token, setToken] = useState('');
  const [user, setUser] = useState(null);

  return (
    // AuthContext.Providerでラップする
    <AuthContext.Provider value={{ token, setToken, user, setUser}}>
      {children}
    </AuthContext.Provider>
  );
};
