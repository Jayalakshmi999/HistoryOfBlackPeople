package com.example.demo.repos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


import com.example.demo.entities.User;

public interface UserRepository extends MongoRepository<User,String> {
	
	User findByEmail(String email);

	 User findBy_id(String _id);
		// TODO Auto-generated method stub
		

}
