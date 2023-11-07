package com.example.demo.entities;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfOutput implements Comparable<PdfOutput> {
	private String path;
	private String content;
	private Map<String, List<Integer>> wordslist;
	private Map <String,Integer> count;
	@Override
	public int compareTo(PdfOutput other) {
		// TODO Auto-generated method stub
		Collection<Integer> x = this.count.values();
		Collection<Integer> y = other.count.values();
		int sum1=0;
		int sum2=0;
		for(int i:x) {
			sum1+=i;
		}
		for(int i:y) {
			sum2+=i;
		}
		return Integer.compare(sum2, sum1);
	}

}
