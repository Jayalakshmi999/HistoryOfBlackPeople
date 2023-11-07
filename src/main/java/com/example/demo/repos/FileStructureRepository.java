package com.example.demo.repos;

import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entities.FileStructure;

public interface FileStructureRepository extends MongoRepository<FileStructure, String>  {

	FileStructure findByMagazineNameAndYear(String magazineName, int year);
	
	List<FileStructure> findByPdfStructureContentContainingIgnoreCase(String content);
	List<FileStructure> findByPdfStructureContentRegex(String phrase);
	List<FileStructure> findByPdfStructureContent(String phrase);
//	@Query("{'pdfStructure.content': { $regex: ?0, $options: 'i' }}")
//    List<String> findMatchingWordsInPdfStructureContent(String phrase);
//

	
//	@Query("{\"match\":{\"pdfStructure.content\":{\"query\":\"?0\",\"fuzziness\":\"AUTO\"}}}")
//	List<FileStructure> findByPdfStructureContentMatchingPhraseWithFuzziness(String phrase);
//	
//	@Query("{\"match\":{\"pdfStructure.content\":{\"query\":\"?0\",\"fuzziness\":\"AUTO\"}}}")
//	List<String> findMatchingWordsInPdfStructureContentWithFuzziness(String phrase);
//
//	@Query("{\"term\": {\"pdfStructure.content\": \"?0\"}}")
//	List<FileStructure> findByExactKeyword(String keyword);
//	
//	@Query("{\"match_phrase\": {\"pdfStructure.content\": \"?0\"}}")
//	List<FileStructure> findByExactSentence(String sentence);
	
//	@Query("{'pdfStructure.content': ?0}")
//	List<FileStructure> findByExactKeyword(String keyword);
//
//	@Query("{'pdfStructure.content': {$regex: ?0, $options: 'i'}}")
//	List<FileStructure> findByExactSentence(String sentence);
//
//	
	
	FileStructure deleteByMagazineNameIgnoreCase(String fileName);

	

}
