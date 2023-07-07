import '../App.css';

import { Link } from 'react-router-dom';
import React, { useContext, useState } from 'react';
import axios from 'axios';

import { saveAs } from 'file-saver';

import { AuthContext } from '../Auth/AuthContext';

function UserSearch() {
  const [users, setUsers] = useState([]);
  const [searchName, setSearchName] = useState('');
  const { token } = useContext(AuthContext);

  const handleSearchClick = async () => {
    try {
      console.log(`before search (${token})`);
      console.log("REACT_APP_API_URL=" + process.env.REACT_APP_API_URL);
      const response = await axios.get(process.env.REACT_APP_API_URL + '/api/users/search', {
        headers: {
          Authorization: `Bearer ${token}`
        },
        params: {
          name: searchName,
        },
      });
      setUsers(response.data);
      console.log("after search");
    } catch (error) {
      console.error('Error fetching data', error);
    }
  };

  const handleNameInputChange = (event) => {
    setSearchName(event.target.value);
  };

  const handlePrintExcel = async () => {
    const ExcelJS = require('exceljs');
    console.log("excel users:" + users);
    // Load the Excel template
    const response = await fetch(`${process.env.PUBLIC_URL}/template/user-list.xlsx`);
    const arrayBuffer = await response.arrayBuffer();
    const data = new Uint8Array(arrayBuffer);
    const workbook = new ExcelJS.Workbook();
    await workbook.xlsx.load(data);
    const worksheet = workbook.getWorksheet('Sheet1');
    const sourceRow = worksheet.getRow(3);

    const dateTimeOptions = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' };
    const dateTimeFormat = new Intl.DateTimeFormat('ja-JP', dateTimeOptions);

    // Fill the Excel template with data
    users.forEach((user, index) => {
      const row = index + 3; // Assuming the data starts at row 3
      console.log("excel row=" + row.toString());
      const targetRow = worksheet.getRow(row);
      if (row !== 3) {
        sourceRow.eachCell({ includeEmpty: true }, (cell, colNumber) => {
          const targetCell = targetRow.getCell(colNumber);
          targetCell.style = cell.style; // Copy cell format
        });
      }

      targetRow.getCell('B').value = user.id;
      targetRow.getCell('C').value = user.name;
      targetRow.getCell('D').value = user.dept;
      targetRow.getCell('E').value = user.title;
      targetRow.getCell('F').value = user.insert_id;
      targetRow.getCell('G').value = dateTimeFormat.format(new Date(user.insert_time));
      targetRow.getCell('H').value = user.update_id;
      targetRow.getCell('I').value = dateTimeFormat.format(new Date(user.update_time));
    });
    // Write the workbook (Excel file)
    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new Blob([buffer], { type: 'application/octet-stream' });
    saveAs(blob, 'user-list.xlsx');
  };

  const handlePrintPDF = async () => {
    try {
      const response = await axios.get(process.env.REACT_APP_API_URL + '/api/users/pdf', {
        headers: {
          Authorization: `Bearer ${token}`
        },
        params: {
          name: searchName,
        },
        responseType: 'blob', // 告诉 axios 响应类型为二进制数据
      });
console.log("pdf response:" + response);
      // 使用 FileSaver.js 保存文件
      saveAs(response.data, 'user-list.pdf');
    } catch (error) {
      console.error('Error downloading file', error);
    }
  };
  return (
    <div>
      <label htmlFor="searchName">ユーザ名:&nbsp;&nbsp;</label>
      <input
        type="text"
        id="searchName"
        value={searchName}
        onChange={handleNameInputChange}
      />
      &nbsp;&nbsp;<button onClick={handleSearchClick}>　検索　</button>
      &nbsp;&nbsp;<button onClick={handlePrintExcel}>Excel保存</button>
      &nbsp;&nbsp;<button onClick={handlePrintPDF}>PDF保存</button>
      <p></p>
      <table className="common-list" align="center">
        <thead>
          <tr>
            <th>ユーザID</th>
            <th>ユーザ名</th>
            <th>部門</th>
            <th>役職</th>
            <th>登録者ID</th>
            <th>登録日時</th>
            <th>更新者ID</th>
            <th>更新日時</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>
                <Link to={`/userupdate/${user.id}`}>
                  {user.id}
                </Link>
              </td>
              <td>{user.name}</td>
              <td>{user.dept}</td>
              <td>{user.title}</td>
              <td>{user.insert_id}</td>
              <td>{user.insert_time}</td>
              <td>{user.update_id}</td>
              <td>{user.update_time}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default UserSearch;
