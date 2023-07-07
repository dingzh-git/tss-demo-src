import React, { useState, useContext } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../Auth/AuthContext';

function Login({ updateLoginStatus }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  // AuthContextからsetTokenを取得する
  const { setUser, setToken } = useContext(AuthContext);

  const getValues = {
    id: username,
    password: password
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      var data = getValues;
      const response = await axios.post(process.env.REACT_APP_API_URL + '/api/auth/login', data);

      // If the server responds with a success status, update the login status and navigate to the main page.
      if (response.status === 200) {
        const user = response.data.user;
        const tok = response.data.token;
        //console.log("login ok: " + user.dept + "--" + tok);
        setToken(tok);
        setUser(user);
        updateLoginStatus(true);
        navigate('/usersearch');
      } else {
        alert(response.data);
      }
    } catch (error) {
      console.error('Login failed:', error);
      alert('Login failed: Invalid username or password', error.getValues);
    }
  };

  return (
    <div>
      <h1>ログイン画面</h1>
      <form onSubmit={handleSubmit}>
        <table align="center">
          <tbody>
            <tr>
              <td align="right">ユーザID:</td>
              <td><input type="text" value={username} onChange={(e) => setUsername(e.target.value)} /></td>
            </tr>
            <tr>
              <td align="right">パスワード:</td>
              <td><input type="password" value={password} onChange={(e) => setPassword(e.target.value)} /></td>
            </tr>
            <tr><td colSpan={2}></td></tr>
            <tr><td colSpan={2}><input type="submit" value="ログイン" /></td></tr>
          </tbody></table>
      </form>
    </div>
  );
}

export default Login;
