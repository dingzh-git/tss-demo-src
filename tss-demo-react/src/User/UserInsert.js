import React, { useContext, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';

import { AuthContext } from '../Auth/AuthContext';

function UserInsert() {

  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors }, getValues } = useForm();
  const { token } = useContext(AuthContext);
  const [role, setRole] = useState('');
  const roles = ['admin', 'user'];

  const handleRoleChange = (event) => {
    setRole(event.target.value);
  };

  const onSubmit = async (data) => {
    try {
      await axios.post(`${process.env.REACT_APP_API_URL}/api/users/insert`,
        data,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
      console.log("handleInsert successed");
      navigate('/usersearch');
    } catch (error) {
      if (error.response && error.response.status === 409) {
        alert('ユーザ登録失敗（ユーザID重複）');
      } else {
        alert('ユーザ登録失敗:' + error);
      }
      console.error('登録失敗:' + error);
    }
  };

  const handleCancel = () => {
    navigate('/usersearch');
  };

  return (
    <div>
      <h1>ユーザ登録</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <table align="center">
          <tbody>
            <tr>
              <td align="right">ID:</td>
              <td>
                <input type="text" name="id"
                  {...register('id', {
                    required: true,
                    pattern: {
                      value: /^\d+$/,
                      message: "IDを5桁半角数字で入力してください"
                    },
                    minLength: 5, maxLength: 5
                  })}
                />
                {errors.id && <p>IDを5桁半角数字で入力してください</p>}
              </td>
            </tr>
            <tr>
              <td align="right">パスワード:</td>
              <td>
                <input type="password" name="password"
                  {...register('password', { required: true, minLength: 8, maxLength: 16 })}
                />
                {errors.password && <p>パスワードを8～16桁で入力してください</p>}
              </td>
            </tr>
            <tr>
              <td align="right">パスワード確認:</td>
              <td>
                <input type="password" name="confirmPassword"
                  {...register('confirmPassword', {
                    required: true,
                    validate: value => value === getValues('password') || '2回目のパスワード入力が一致していません'
                  })}
                />
                {errors.confirmPassword && <p>{errors.confirmPassword.message}</p>}
              </td>
            </tr>
            <tr>
              <td align="right">氏名:</td>
              <td>
                <input type="text" name="name"
                  {...register('name', { required: true, maxLength: 10 })}
                />
                {errors.name && <p>氏名を入力してください</p>}
              </td>
            </tr>
            <tr>
              <td align="right">部門:</td>
              <td>
                <input type="text" name="dept"
                  {...register('dept', { required: true, maxLength: 10 })}
                />
                {errors.dept && <p>部門を入力してください</p>}
              </td>
            </tr>
            <tr>
              <td align="right">役職:</td>
              <td>
                <input type="text" name="title"
                  {...register('title', { required: true, maxLength: 10 })}
                />
                {errors.title && <p>役職を入力してください</p>}
              </td>
            </tr>
            <tr>
              <td align="right">権限:</td>
              <td>
                <select value={role} onChange={handleRoleChange}>
                  <option value="">権限を選択してください...</option>
                  {roles.map((role, index) => (
                    <option key={index} value={role}>
                      {role}
                    </option>
                  ))}
                </select>
              </td>
            </tr>
            <tr><td colSpan="2"></td></tr>
            <tr>
              <td colSpan="2">
                <button type="submit" style={{ width: '30%' }}>登録</button>&nbsp;&nbsp;
                <button type="button" style={{ width: '30%' }} onClick={handleCancel}>キャンセル</button>
              </td>
            </tr>
          </tbody>
        </table>
      </form>
    </div >
  );
}

export default UserInsert;
