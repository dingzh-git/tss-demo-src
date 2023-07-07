package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.User;

@Mapper
public interface UserMapper {

	User getUser(String id);
	
	List<User> searchUserByName(@Param("name") String name);
	
	List<User> getAllUsers();
	
	int updateUser(User user);

	int insertUser(User user);

	int deleteUser(User user);
	
	int deleteUserForTest(User user);

}
