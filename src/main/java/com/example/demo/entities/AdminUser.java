package com.example.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Document(collection="AdminUser")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser {
	@Id
	private String _id;
	private String email;
	private String password;

}
