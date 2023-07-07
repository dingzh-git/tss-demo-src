package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.jodconverter.core.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.security.NoPermissionException;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/api/users/get/{id}")
	public User getUser(@PathVariable String id) {
		return userService.getUser(id);
	}

	@GetMapping("/api/users/search")
	public List<User> searchUser(@RequestParam(required = false) String name) {
		return userService.searchUser(name);
	}

	@PutMapping("/api/users/update/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody User user) {
		try {
			userService.updateUser(userId, user);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoPermissionException e) {
			log.info("update no permission:" + e);
			return new ResponseEntity<>("no permission: update user ID.", HttpStatus.FORBIDDEN);
		} catch (OptimisticLockingFailureException e) {
			log.info("update failed:" + e);
			return new ResponseEntity<>("Insert failed: update user ID.", HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.info("Insert failed: system error: " + e);
			return new ResponseEntity<>("Insert failed: system error.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/api/users/insert")
	public ResponseEntity<?> insertUser(@RequestBody User user) {
		try {
			userService.insertUser(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (DuplicateKeyException e) {
			log.info("Insert failed: duplicate user ID.");
			return new ResponseEntity<>("Insert failed: duplicate user ID.", HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.info("Insert failed: system error: " + e);
			return new ResponseEntity<>("Insert failed: system error.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/api/users/delete/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable String userId, @RequestBody User user) {
		try {
			userService.deleteUser(userId, user);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (OptimisticLockingFailureException e) {
			log.info("update failed:" + e);
			return new ResponseEntity<>("Insert failed: delete user ID.", HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.info("Insert failed: system error: " + e);
			return new ResponseEntity<>("Insert failed: system error.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
    @GetMapping("/api/users/pdf")
    public ResponseEntity<byte[]> users2PDF(@RequestParam(required = false) String name) throws IOException, OfficeException {
    	ByteArrayOutputStream os = userService.users2PDF(name);
    	
    	// Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "user-list.pdf");
        // Return the PDF byte array as ResponseEntity
        return new ResponseEntity<>(os.toByteArray(), headers, HttpStatus.OK);
    }
}
