package com.example.demo.repos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entities.AdminUser;

public interface AdminRepository extends MongoRepository<AdminUser, String> {

	
	AdminUser findByEmail(String email);
}
