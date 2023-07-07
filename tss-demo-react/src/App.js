import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { AuthProvider } from './Auth/AuthContext';
import UserSearch from './User/UserSearch';
import UserUpdate from './User/UserUpdate';
import UserInsert from './User/UserInsert';
import Other from './Other/Other';
import Login from './User/Login';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const updateLoginStatus = (status) => {
    setIsLoggedIn(status);
  };

  return (
    <Router>
      <div className="App">
        <AuthProvider>
          {isLoggedIn ? (
            <>
              <nav>
                <ul>
                  <li>
                    <Link to="/userinsert">ユーザ登録</Link>
                  </li>
                  <li>
                    <Link to="/usersearch">ユーザ検索</Link>
                  </li>
                  <li>
                    <Link to="/other">その他</Link>
                  </li>
                </ul>
              </nav>
              <Routes>
                <Route path="/userinsert" element={<UserInsert />} />
                <Route path="/usersearch" element={<UserSearch />} />
                <Route path="/userupdate/:userId" element={<UserUpdate />} />
                <Route path="/other" element={<Other />} />
                <Route path="*" element={<UserSearch />} />
              </Routes>
            </>
          ) : (
            <Login updateLoginStatus={updateLoginStatus} />
          )}
        </AuthProvider>
      </div>
    </Router>
  );
}

export default App;
