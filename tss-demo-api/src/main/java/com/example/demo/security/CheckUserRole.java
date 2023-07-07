package com.example.demo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.model.User;

public class CheckUserRole {

	public static final String roleAdmin = "admin";
	public static final String roleUser = "user";

	public static boolean isAdmin(String role) {
		return roleAdmin.equalsIgnoreCase(role);
	}

	public static boolean isUser(String role) {
		if (role == null || role.isEmpty()) {
			return false;
		}
		return role.toLowerCase().startsWith(roleUser);
	}

	public static List<User> filterUser(User currentUser, List<User> users) {

		if (currentUser != null) {
			if (isAdmin(currentUser.getRole())) {
				return users;
			}
			if (isUser(currentUser.getRole())) {
				ArrayList<User> newUsers = new ArrayList<User>();
				for (User user : users) {
					if (user.getDept().equals(currentUser.getDept())) {
						newUsers.add(user);
					}
				}
				return newUsers;
			}
		}
		return new ArrayList<User>();
	}

	public static void checkUpdatePermission(User currentUser, User user) throws NoPermissionException {

		if (currentUser == null || !isAdmin(currentUser.getRole())) {
			throw new NoPermissionException("no permission");
		}

	}

	public static String getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return username;
	}
}
