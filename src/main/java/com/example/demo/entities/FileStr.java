package com.example.demo.entities;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileStr {
	private String _id;
	private String authorName;
	private int year;
	private String fileName;
	private String filePath;
	private String type;
	private String description;

}
