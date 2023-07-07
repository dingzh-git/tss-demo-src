import React from 'react';
import AuthProvider from './Auth/AuthProvider';
import App from './App';

function Root() {
  return (
    <AuthProvider>
      <App />
    </AuthProvider>
  );
}

export default Root;
