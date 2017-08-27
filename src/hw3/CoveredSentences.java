package hw3;

import java.util.ArrayList;
import java.util.HashMap;

public class CoveredSentences {
	
	private ArrayList<String> predicateNames = new ArrayList<String>();
	private HashMap<String, ArrayList<DisjunctionSentence>> hashMap = new HashMap<String, ArrayList<DisjunctionSentence>>();
	
	public boolean isCovered(DisjunctionSentence disjunctionSentence){
		String key = getKey(disjunctionSentence);
		if(hashMap.containsKey(key)){
			ArrayList<DisjunctionSentence> sentences = hashMap.get(key);
			//Need to do a deep check
			for(DisjunctionSentence sentence : sentences){
				if(sentence.isSameAs(disjunctionSentence)){
					return true;
				}
			}
		}
		return false;
	}
	
	public void addToCoveredSentences(DisjunctionSentence disjunctionSentence){
		String indexes = getKey(disjunctionSentence);
		if(hashMap.containsKey(indexes)){
			ArrayList<DisjunctionSentence> arrayList = hashMap.get(indexes);
			arrayList.add(disjunctionSentence);
		}
		else{
			ArrayList<DisjunctionSentence> arrayList = new ArrayList<DisjunctionSentence>();
			arrayList.add(disjunctionSentence);
			hashMap.put(indexes, arrayList);
		}
	}

	private String getKey(DisjunctionSentence disjunctionSentence) {
		ArrayList<KBSentence> subsentences = disjunctionSentence.getSubsentences();
		String indexes = "";
		for(KBSentence subsentence: subsentences){
			String predicateName = PredicateToSentence.getPredicateFromKBSentence(subsentence).getName();
			int index = predicateNames.indexOf(predicateName);
			if(index == -1){
				predicateNames.add(predicateName);
				index = predicateNames.indexOf(predicateName);
			}
			indexes += index;
		}
		return indexes;
	}

}
