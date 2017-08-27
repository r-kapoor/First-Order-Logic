

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import hw3.Input;
import hw3.KBSentence;
import hw3.KnowledgeBase;
import hw3.PredicateToSentence;
import hw3.Sentence;

public class homework {
	
	public static void main(String args[]) throws IOException{
		Input input = new Input();
		KnowledgeBase kb = new KnowledgeBase();
		addInputToKB(input, kb);
		kb.convertToCNF();
		kb.createSeparateClauses();
//		System.out.println("After Separation");
//		System.out.println(kb);
		kb.standardizeVariables();
//		System.out.println("After Standardize");
//		System.out.println(kb);
		KnowledgeBase queries = new KnowledgeBase();
		addQueriesToQueryKB(input, queries);
//		System.out.println("The queries:");
//		System.out.println(queries);
		ArrayList<Boolean> results = processQueries(kb, queries);
		Input.writeOutput(results);
	}

	private static ArrayList<Boolean> processQueries(KnowledgeBase kb, KnowledgeBase queries) {
		ArrayList<Boolean> results = new ArrayList<Boolean>();
		ArrayList<KBSentence> querySentences = queries.getSentences();
		for(KBSentence query : querySentences){
			KnowledgeBase kbClone = kb.clone();
			PredicateToSentence kbIndex = new PredicateToSentence(kbClone);
//			System.out.println(kbIndex);
			Sentence querySentence = query.getSentence();
			kbIndex.addQueryToKBIndex(querySentence);
			boolean resolutionResult = true;
			try{
				resolutionResult = kbIndex.runResolution();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(resolutionResult);
			results.add(resolutionResult);
//			break;//Need to make a copy of kb as it is modified
		}
		return results;
	}

	private static void addQueriesToQueryKB(Input input, KnowledgeBase queries) {
		Iterator<String> iterator = input.queryStrings.iterator();
		while(iterator.hasNext()){
			String sentenceString = iterator.next();
			if(sentenceString.indexOf('~') != -1){
				sentenceString = "(" + sentenceString + ")";
			}
			KBSentence sentence = new KBSentence(sentenceString);
			queries.add(sentence);
		}
	}

	private static void addInputToKB(Input input, KnowledgeBase kb) {
		Iterator<String> iterator = input.sentenceStrings.iterator();
		while(iterator.hasNext()){
			String sentenceString = iterator.next();
//			System.out.println(sentenceString);
			KBSentence sentence = new KBSentence(sentenceString);
			kb.add(sentence);
		}
	}

}
