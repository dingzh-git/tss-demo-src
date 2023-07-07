import React, { useContext, useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';

import { AuthContext } from '../Auth/AuthContext';

function UserUpdate() {
  const { userId } = useParams();
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors }, setValue, getValues } = useForm();
  const { token } = useContext(AuthContext);
  const [role, setRole] = useState('');
  const roles = ['admin', 'user'];

  useEffect(() => {
    const getUser = async () => {
      try {
        const response = await axios.get(`${process.env.REACT_APP_API_URL}/api/users/get/${userId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
        for (let key in response.data) {
          setValue(key, response.data[key]);
        }
        setValue('confirmPassword', getValues('password'));
        setValue('role', response.data.role);
      } catch (error) {
        console.error('Failed to fetch user:', error);
      }
    };
    getUser();
  }, [userId, setValue, getValues, token]);

  const handleRoleChange = (event) => {
    setRole(event.target.value);
  };

  const onSubmit = async (data) => {
    try {
      if (window.confirm('ユーザを更新します、よろしいですか？')) {
        const response = await axios.put(`${process.env.REACT_APP_API_URL}/api/users/update/${userId}`,
          data,
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
        if (response.data.version !== data.version) {
          alert('別の操作よりこのデータを更新しました。');
          return;
        }
        navigate('/usersearch');
      }
    } catch (error) {
      alert('ユーザ更新失敗:' + error);
      console.error('ユーザ更新失敗:' + error);
    }
  };

  const handleDelete = async (event) => {
    try {
      event.preventDefault();
      if (window.confirm('ユーザを削除します、よろしいですか？')) {
        await axios.put(`${process.env.REACT_APP_API_URL}/api/users/delete/${userId}`, getValues());
        navigate('/usersearch');
      }
    } catch (error) {
      alert('ユーザ削除失敗:' + error);
      console.error('ユーザ削除失敗:' + error);
    }
  };

  const handleCancel = () => {
    navigate('/usersearch');
  };

  return (
    <div>
      <h1>ユーザ更新</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <table align="center">
          <tbody>
            <tr>
              <td align="right">ID:</td>
              <td>
                <input type="text" name="id" readOnly
                  {...register('id')} />
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
                <select defaultValue={role} onChange={handleRoleChange}>
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
                <button style={{ width: '30%' }} type="submit">更新</button>&nbsp;&nbsp;
                <button style={{ width: '30%' }} onClick={handleDelete}>削除</button>&nbsp;&nbsp;
                <button style={{ width: '30%' }} onClick={handleCancel}>キャンセル</button>
              </td>
            </tr>
          </tbody>
        </table>
      </form>
    </div>
  );
}

export default UserUpdate;
