package com.example.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="FileDoc")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {
	@Id
	private String _id;
	private String authorName;
	private int year;
	private String fileName;
	private String filePath;
	private String type;
	private String description;

}
