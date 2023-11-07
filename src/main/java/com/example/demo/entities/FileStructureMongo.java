package com.example.demo.entities;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="FileDocumentation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileStructureMongo {
	private String textpath;
	private String magazineName;
	private int year;

private List<PdfStructure>pdfStructure;
}
