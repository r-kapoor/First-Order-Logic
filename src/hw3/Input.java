package hw3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Input {
	int numberOfQueries, numberOfSentences;
	public ArrayList<String> queryStrings;
	public ArrayList<String> sentenceStrings;
	public Input() throws IOException{
		queryStrings = new ArrayList<String>();
		sentenceStrings = new ArrayList<String>();
		File file = new File("input.txt");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line = null;
		line = bufferedReader.readLine();
		numberOfQueries = Integer.parseInt(line);
		for(int i = 0; i < numberOfQueries; i++){
			line = bufferedReader.readLine();
			queryStrings.add(line);
		}
		line = bufferedReader.readLine();
		numberOfSentences = Integer.parseInt(line);
		for(int i = 0; i < numberOfSentences; i++){
			line = bufferedReader.readLine();
			sentenceStrings.add(line);
		}
		bufferedReader.close();
	}
	
	public static void writeOutput(ArrayList<Boolean> results) throws IOException {
		FileWriter fw = new FileWriter("output.txt");
		for(Boolean result : results){
			fw.write(result.toString().toUpperCase() +"\n");
		}
		fw.close();
	}

}
