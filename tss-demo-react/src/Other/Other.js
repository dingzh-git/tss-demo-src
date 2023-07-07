import '../App.css';

import React, { useState } from 'react';
import axios from 'axios';

function Other() {
  const [userId, setUserId] = useState('');
  const [user, setUser] = useState(null);

  const handleSearch = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_API_URL}/api/users/get/${userId}`);
      setUser(response.data);
    } catch (error) {
      console.error('Failed to fetch user', error);
      setUser(null);
    }
  };

  return (
    <div>
      <input
        type="text"
        value={userId}
        onChange={event => setUserId(event.target.value)}
        placeholder="Enter user ID"
      />
      <button onClick={handleSearch}>Search</button>

      {user && (
        <div>
          <p>ID: {user.id}</p>
          <p>Name: {user.name}</p>
          <p>Department: {user.dept}</p>
          <p>Insert ID: {user.insert_id}</p>
          <p>Insert Time: {user.insert_time}</p>
          <p>Update ID: {user.update_id}</p>
          <p>Update Time: {user.update_time}</p>
        </div>
      )}
    </div>
  );
}

export default Other;
