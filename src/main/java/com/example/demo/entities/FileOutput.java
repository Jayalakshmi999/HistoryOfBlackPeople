package com.example.demo.entities;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileOutput implements Comparable <FileOutput> {
	private String filePath;
	private String magazineName;
	private int year;
	private Map<String,Integer> finalwords;
	private Set<PdfOutput> pdfOutputs;
	@Override
	public int compareTo(FileOutput other) {
		// TODO Auto-generated method stub
		
		 Collection<Integer> f = this.finalwords.values();
		 Collection<Integer> o =  other.finalwords.values();
		 int sum1=0;
		 int sum2=0;
		 for(int i:f) {
			 sum1+=i;
		 }
		 for(int i:o) {
			 sum2+=i;
		 }
		return Integer.compare(sum2,sum1);
	}

}
