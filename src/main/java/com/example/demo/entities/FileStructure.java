package com.example.demo.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="fileStructure")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileStructure {
	@Id
	private String id;
	private String textpath;
	private String magazineName;
	private int year;

private List<PdfStructure>pdfStructure;
}
