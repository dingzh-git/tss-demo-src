package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final UserMapper userMapper;

	public CustomUserDetailsService(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        User user = userMapper.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // 返回一个 CustomUserDetails 对象
        return new CustomUserDetails(user);
    }
}
