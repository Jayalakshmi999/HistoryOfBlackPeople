package com.example.demo.repos;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;



import com.example.demo.entities.FileStructureMongo;

public interface FileStructureMongoRepository extends MongoRepository<FileStructureMongo, ObjectId> {

}
