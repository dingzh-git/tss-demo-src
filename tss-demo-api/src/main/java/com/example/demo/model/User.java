package com.example.demo.model;

import java.util.Date;

import lombok.Data;

@Data
public class User {
	private String id;
	private String password;
    private String name;
    private String dept;
    private String title;
    private String role;
    private String insert_id;
    private Date insert_time;
    private String update_id;
    private Date update_time;
}
