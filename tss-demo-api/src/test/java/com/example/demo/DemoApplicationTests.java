package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private MockMvc mockMvc;
	
	private static String getJson(String path) throws IOException {
		return Files.readString(Paths.get(path), Charset.forName("UTF8"));
	}

	@Test
	public void testInsertDataAndGetUserById() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		String jsonString = getJson("src/test/resources/json/Test001.json");
		User user = objectMapper.readValue(jsonString, User.class);

		// データ登録
		userMapper.insertUser(user);

		// API呼出
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/users/get/{id}", user.getId()))
				.andReturn();
		
		// データ削除
		userMapper.deleteUserForTest(user);
		
		// データ削除確認
		User delUser = userMapper.getUser(user.getId());
		assertTrue(delUser == null);

		String resultStr = result.getResponse().getContentAsString();
		User userResult = objectMapper.readValue(resultStr, User.class);
		assertEquals(user.getId(), userResult.getId());
		assertEquals(user.getName(), userResult.getName());
		assertEquals(user.getDept(), userResult.getDept());
		assertEquals(user.getTitle(), userResult.getTitle());
		assertEquals(user.getRole(), userResult.getRole());
		
	}

}
