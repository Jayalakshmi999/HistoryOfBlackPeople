package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.FileEntity;
import com.example.demo.repos.FileRepository;



@RestController
@CrossOrigin(origins = "https://historyofblackpeople-8zy0.onrender.com/", methods = { RequestMethod.DELETE,RequestMethod.GET,RequestMethod.POST })

public class FileController {
	
	@Autowired
    private FileRepository fileRepository;
	
	
	
	
	@PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("description") String description,
                                   @RequestParam("author") String authorName,
                                   @RequestParam("year") int year) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String[] values=fileName.split(".",-1);
            for(String i : values) {
            	System.out.println(i);
            }
            System.out.println(values.length);
            File uploadDir = new File("C:\\Users\\s556452\\Desktop\\GDP-TEAM3-Database-12-00\\public");
            uploadDir.mkdirs();
            File destFile = new File(uploadDir, fileName);
            file.transferTo(destFile);
            
            FileEntity fileEntity = new FileEntity();
            fileEntity.setType(values[(values.length)-1]);
            fileEntity.setFileName(fileName);
            fileEntity.setAuthorName(authorName);
            fileEntity.setDescription(description);
            fileEntity.setFilePath( fileName);
            fileEntity.setYear(year);
            fileRepository.save(fileEntity);

            return "uploaded";
        } else {
            return "not uploaded";
        }
        
    }
	
	@GetMapping("/search")
	public Set<FileEntity> search(@RequestParam("word") String words){
		List <FileEntity> files=new ArrayList<FileEntity>();
		String[] lists=words.split("&");
		List<String> list = new ArrayList<>(Arrays.asList(lists));
		for(String word:list){
		files.addAll(fileRepository.findByDescriptionContainingIgnoreCaseOrAuthorNameContainingIgnoreCaseOrFileNameContainingIgnoreCase(word,word,word));
		}
		 Set<FileEntity> set = new HashSet<FileEntity>(files);
		 return set;
	}
	
	@DeleteMapping("/delete")
	public FileEntity deleteByname(@RequestParam("filename") String fileName) {
		return fileRepository.deleteByFileName(fileName);
	}
	
	@GetMapping("/byfileName")
	public List<FileEntity> sortByFileName(@RequestParam("word") String word){
		return fileRepository.findByDescriptionContainingIgnoreCaseOrAuthorNameContainingIgnoreCaseOrFileNameContainingIgnoreCaseOrderByFileNameAsc(word,word,word);
	}
	
	@GetMapping("/byauthorName")
	public List<FileEntity> sortByAuthorName(@RequestParam("word") String word){
		return fileRepository.findByDescriptionContainingIgnoreCaseOrAuthorNameContainingIgnoreCaseOrFileNameContainingIgnoreCaseOrderByAuthorNameAsc(word,word,word);
	}
	
	@GetMapping("/byYear")
	public List<FileEntity> sortByYear(@RequestParam("word") String word){
		return fileRepository.findByDescriptionContainingIgnoreCaseOrAuthorNameContainingIgnoreCaseOrFileNameContainingIgnoreCaseOrderByYearAsc(word,word,word);
	}
	public String readTextFile(String filePath) throws IOException {
	    StringBuilder content = new StringBuilder();
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            content.append(line).append("\n");
	        }
	    }
	    return content.toString();
	}
	@GetMapping("/read")
		
		public String extractTextFromPDF() throws IOException {
			String filePath = "C:\\Users\\s556452\\Downloads\\1911Aug.pdf";
		
	        try (PDDocument document = PDDocument.load(new File(filePath))) {
	            PDFTextStripper pdfTextStripper = new PDFTextStripper();
	            return pdfTextStripper.getText(document);
	        }
	}
	
	
	
	
	
	
	
	
	


}
