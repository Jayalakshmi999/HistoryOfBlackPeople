package com.example.demo.repos;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entities.FileEntity;

public interface FileRepository extends MongoRepository<FileEntity,ObjectId> {
	
	List<FileEntity> findByDescriptionContainingIgnoreCaseOrAuthorNameContainingIgnoreCaseOrFileNameContainingIgnoreCase(String word,String word1,String word2);
	
	FileEntity deleteByFileName(String name);
	
	List<FileEntity> findByDescriptionContainingIgnoreCaseOrAuthorNameContainingIgnoreCaseOrFileNameContainingIgnoreCaseOrderByFileNameAsc(String word,String word1,String word2);

	List<FileEntity> findByDescriptionContainingIgnoreCaseOrAuthorNameContainingIgnoreCaseOrFileNameContainingIgnoreCaseOrderByAuthorNameAsc(String word,String word1,String word2);

	List<FileEntity> findByDescriptionContainingIgnoreCaseOrAuthorNameContainingIgnoreCaseOrFileNameContainingIgnoreCaseOrderByYearAsc(String word,String word1,String word2);

}
