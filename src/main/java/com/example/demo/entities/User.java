package com.example.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	private String _id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private boolean isAdmin;

}
