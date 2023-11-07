package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.example.demo.entities.FileEntity;
import com.example.demo.entities.FileOutput;
import com.example.demo.entities.FileStructure;
import com.example.demo.entities.FileStructureMongo;
import com.example.demo.entities.PdfOutput;
import com.example.demo.entities.PdfStructure;
import com.example.demo.repos.FileStructureMongoRepository;
import com.example.demo.repos.FileStructureRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;


@RestController
@RequestMapping("file")
@CrossOrigin("*")
public class FileStructureController {
	@Autowired
	private FileStructureRepository fileStructureRepository;
	
	@Autowired
	private FileStructureMongoRepository fileStructureMongoRepository;
	
	
	@PostMapping("uploadfile")
	public FileStructure upload(@RequestParam("pdffile") MultipartFile file,@RequestParam("textfile") MultipartFile txtfile,
            @RequestParam("magazineName") String magazineName,
      
            @RequestParam("year") int year) throws IOException {
		
		PdfStructure ps=new PdfStructure();
		
		FileStructure fs=new FileStructure();
		String fileName="";
		String txtFileName="";
		
		 if (!file.isEmpty()) {
	             fileName = file.getOriginalFilename();
	             txtFileName=txtfile.getOriginalFilename();
	            
	            }
	            
	            File uploadDir = new File("C:\\Users\\s556452\\Desktop\\GDP-TEAM3-Database-12-00\\public");
	            uploadDir.mkdirs();
	            File destFile = new File(uploadDir, fileName);
	            File dfile=new File(uploadDir,txtFileName);
	            try {
					file.transferTo(destFile);
					txtfile.transferTo(dfile);
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            String fileex="C:\\users\\s556452\\Downloads\\files\\"+fileName;
	            ps.setPdfFilePath(fileName);
	            fs.setTextpath(txtFileName);
	            System.out.println(fileName);
	            String s="";
//	            try (PDDocument document = PDDocument.load(new File(fileex))) {
//		            PDFTextStripper pdfTextStripper = new PDFTextStripper();
//		           
//		            s= pdfTextStripper.getText(document).
//		            		replaceAll("\\s+", " ").trim();
//		           
//		        }
	            
	            PdfReader pdfReader = new PdfReader(fileex);

	            // Initialize a PdfDocument object
	            PdfDocument pdfDocument = new PdfDocument(pdfReader);

	            // Iterate through each page and extract text
	            for (int pageNum = 1; pageNum <= pdfDocument.getNumberOfPages(); pageNum++) {
	            	System.out.println("pdf page");
	                PdfPage page = pdfDocument.getPage(pageNum);
	                String pageText = PdfTextExtractor.getTextFromPage(page);
	                
	                s+=pageText.replaceAll("\\s+", " ");
	                System.out.println("Page " + pageNum + " Text:\n" + pageText);
	            }

	            // Close the PdfDocument
	            pdfDocument.close();
	           
	            ps.setContent(s);
	            s.toLowerCase();
	           
		FileStructure exist=fileStructureRepository.findByMagazineNameAndYear(magazineName,year);
	     if(exist!=null) {
	    	 exist.getPdfStructure().add(ps);
	    	 fs.setPdfStructure(exist.getPdfStructure());
	    	 fileStructureRepository.save(exist);
	    	 return exist;
	     }
	     else {
	    	 fs.setMagazineName(magazineName);
	    	 fs.setYear(year);
	    	 List<PdfStructure>pds=new ArrayList<PdfStructure>();
	    	 pds.add(ps);
	    	 fs.setPdfStructure(pds);
	    	 fileStructureRepository.save(fs);	
	    	 FileStructureMongo mon=new FileStructureMongo();
	    	 mon.setMagazineName(fs.getMagazineName());
	    	 mon.setTextpath(fs.getTextpath());
	    	 mon.setYear(fs.getYear());
	    	 mon.setPdfStructure(fs.getPdfStructure());
	    	 fileStructureMongoRepository.save(mon);
	}
		
		return fs;
		
	}
	
	@GetMapping("/search")
	public Set<FileStructure> search(@RequestParam("word") String words){
		Set<FileOutput> finalset=new HashSet<>();
		List <FileStructure> files=new ArrayList<FileStructure>();
		
		files.addAll(fileStructureRepository.findByPdfStructureContentContainingIgnoreCase(words));
		
		 Set<FileStructure> set = new HashSet<FileStructure>(files);
		 return set;
	}
	
	@GetMapping("/get")
	public Set<FileOutput> alldoc(){
		
		Iterable<FileStructure> d = fileStructureRepository.findAll();
		LinkedHashSet<FileOutput> t=new LinkedHashSet<>();
		
		for(FileStructure fg:d) {
			FileOutput gu=new FileOutput();
			gu.setFilePath(fg.getTextpath());
			gu.setMagazineName(fg.getMagazineName());
			gu.setYear(fg.getYear());
			gu.setFinalwords(Collections.emptyMap());
			Set<PdfOutput> pdf=new HashSet<>();
			List<PdfStructure>ps=fg.getPdfStructure();
			for(PdfStructure pg:ps) {
				PdfOutput jk=new PdfOutput();
				jk.setPath(pg.getPdfFilePath());
				jk.setContent(pg.getContent());
				jk.setCount(Collections.emptyMap());
				jk.setWordslist(Collections.emptyMap());
				pdf.add(jk);
			}
			gu.setPdfOutputs(pdf);
			t.add(gu);
		}
		return t;
	}
	
	@GetMapping("/phrases")
	public Set<FileOutput> des( @RequestParam("phrase") String phrase){
		Set<FileOutput> finalfo=new TreeSet<>();
		Set <FileStructure> outset=new HashSet<>();
		List<String> words=new ArrayList<>();
		if(phrase.contains("&"))
		{
			words=Arrays.asList(phrase.split("&"));
		}
		else if(phrase.contains("#"))
		{
			words=Arrays.asList(phrase.split("#"));
		}
		else if((fileStructureRepository.findByPdfStructureContentRegex(phrase).size()==0) && (phrase.contains(" ")))
		{
			words=Arrays.asList(phrase.split(" "));
		}
		else {
			words=Arrays.asList(phrase);
		}
		if(phrase.contains("&"))
		{
			String a[]=phrase.split("&");
			for(int str=0;str<a.length;str++) {
			List<FileStructure> b=fileStructureRepository.findByPdfStructureContentRegex(a[str]);
			Set<FileStructure>x=new HashSet<>(b);
			if(str==0)
			{
				outset=x;
			}
			else {
				outset.retainAll(x);
			}
			}
			
		}
		else if(phrase.contains("#")) {
			String a[]=phrase.split("#");
			for(int str=0;str<a.length;str++) {
			List<FileStructure> b=fileStructureRepository.findByPdfStructureContentRegex(a[str]);
			Set<FileStructure>x=new HashSet<>(b);
			if(str==0)
			{
				outset=x;
			}
			else {
				outset.addAll(x);
			}
			}
			
			
		}
		else if((fileStructureRepository.findByPdfStructureContentRegex(phrase).size()==0) && (phrase.contains(" ")))
		{
			System.out.println("hiii");
			String a[]=phrase.split(" ");
			for(int str=0;str<a.length;str++) {
			List<FileStructure> b=fileStructureRepository.findByPdfStructureContentRegex(a[str]);
			Set<FileStructure>x=new HashSet<>(b);
			if(str==0)
			{
				outset=x;
			}
			else {
				outset.retainAll(x);
			}
			}
		}
		
		
		else {
			
		 outset=new HashSet<>(fileStructureRepository.findByPdfStructureContentRegex( phrase));
		}
		
		
		for(FileStructure fs:outset) {
			
			FileOutput fo=new FileOutput();
			fo.setFilePath(fs.getTextpath());
			fo.setMagazineName(fs.getMagazineName());
			fo.setYear(fs.getYear());
			
			List<PdfStructure>ps=fs.getPdfStructure();
			List<PdfOutput>lpo=new ArrayList<>();
			
			
			Map<String,Integer> d=new HashMap<>();
		
				
				int totalcount =0;
				int i=0;
				
			for(PdfStructure p:ps)
			{
				
				System.out.println("hi");
				  
i++;Map<String,Integer> m=new HashMap<>();
Map<String,List<Integer>> k=new HashMap<>();	
				
				PdfOutput po=new PdfOutput();
				for(String w:words) {
				int count=0;
				List<Integer>indices=new ArrayList<>();
				int index=p.getContent().toLowerCase().indexOf(w.toLowerCase());
				while (index != -1) {
		            indices.add(index);
		            index = p.getContent().toLowerCase().indexOf(w.toLowerCase(), index + phrase.length());
		        }
				count=indices.size();
				
				po.setContent(p.getContent());
				po.setPath(p.getPdfFilePath());
				
				 
			  k.put(w, indices);
			  m.put(w,count);
			  po.setWordslist(k);
			  po.setCount(m);	
			 
				totalcount+=count;
		
			}
				 lpo.add(po);
			}
			for(String wo:words) {
				
				int co=0;
				for(PdfOutput l:lpo) {
					System.out.println(l);
					co+=l.getCount().get(wo);
				}
				d.put(wo,co);
			
				
				
			
			
			fo.setFinalwords(d);
			Set<PdfOutput> wy=new TreeSet<>(lpo);
			fo.setPdfOutputs(wy);
			
		
			finalfo.add(fo);
		}

		
		
		}
		return finalfo;
	}
	
	



@DeleteMapping("/delete")
public FileStructure deleteByname(@RequestParam("magazineName") String fileName) {
	return fileStructureRepository.deleteByMagazineNameIgnoreCase(fileName);
}




@GetMapping("/count")
public long count(@RequestParam("phrase") String exactSentence) {
	return fileStructureRepository.findByPdfStructureContent(exactSentence).size();
}

@GetMapping("/sortYear")
public Set<FileOutput> sortYear( @RequestParam("phrase") String phrase){

	
	Set<FileOutput> f=des(phrase);
	List<FileOutput>g=new ArrayList<>(f);
	for(int i=0;i<g.size();i++) {
		for(int j=0;j<g.size();j++) {
			if(g.get(i).getYear()<g.get(j).getYear()) {
				FileOutput u=g.get(i);
				g.set(i, g.get(j));
				g.set(j, u);
			}
		}
		
	}
	LinkedHashSet<FileOutput>res=new LinkedHashSet<>(g);
	return res;

}

//@GetMapping("getkeywords")
//public List<String> findkeywords(@RequestParam("phrase")String phrase){
//	return fileStructureRepository.findMatchingWordsInPdfStructureContent(phrase);
//}
}
	
