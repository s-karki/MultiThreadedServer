package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.*;

import exception.ExceptionHandler;
import mjson.Json;
import java.io.*;

public class Dictionary {
	File filename;
	private final ReadWriteLock lock; 
	HashMap<String, ArrayList<String>> dict;
	
	
	/* results are written to a text file, and loaded into memory 
	 * on startup
	 */
	public Dictionary(File filename) {
		this.filename = filename;
		this.lock = new ReentrantReadWriteLock();
		
	}
	
	// load all current records into memory for faster lookups 
	public void loadIntoMemory() throws FileNotFoundException, IOException {
		HashMap<String, ArrayList<String>> readIn = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String record;
		while((record = reader.readLine()) != null) {
			// parse JSON
			Json j = Json.read(record);
			String word = j.at("word").asString();
			List<Object> ol = j.at("definition").asList();
			ArrayList<String> defs = new ArrayList<>();
			for (Object o : ol) {
				defs.add(o.toString());
			}
			readIn.put(word, defs);		
		}
		dict = readIn; 
		reader.close();
	}
	public ArrayList<String> query(String word) {
		ArrayList<String> res;
		lock.readLock().lock();
		try {
			res = dict.get(word);
		} finally {
			lock.readLock().unlock();
		}
		return res; 
	}
	
	/* query the word – if we find match, we overwrite it
	 * if no match, we add a new record
	 */ 
	public String add(String word, String definition) {		
		if (word == null || word.equals("") || definition == null || definition.equals("")) {
			return "Error: The word or definition you supplied was empty. The dictionary will"
					+ " not be updated"; 
		}
		
		String msg = "";
		lock.writeLock().lock();
		try {
			writeToFile(word, definition);
			msg = "The word " + "\"" + word + "\"" + " and the definition " + "\""
			+ definition + " were successfully added to the dictionary";
		}
		catch (IOException e) {
			ExceptionHandler.printMessage("Error: There was an IO Error "
					+ "writing to the dictionary file during the addition", e);
		} finally {
			lock.writeLock().unlock();
		}
		return msg; 
	}
	
	public String remove(String word) {
		if (word == null || word.equals("")) {
			return "Error: the word you entered was empty. "
					+ "The dictionary will not be updated";
		}
		if(dict.get(word) == null) {
			return "The word you entered was not in the dictionary, and could "
					+ "not be removed.";
		}	
		String msg = "";
		lock.writeLock().lock();
		try {
			dict.remove(word);
			removeFileRecord(word);
			msg = "The word and its definition were successfully removed"; 
		} 
		catch (IOException e) {
			ExceptionHandler.printMessage("Error: There was an IO Error "
					+ "writing to the dictionary file during the deletion", e);
		}
		finally {
			lock.writeLock().unlock();
		}
		return msg; 
	}
		
	/*	Helper methods to support add and remove */
	
	/* Write to the dictionary file 
	 * There are two types of write: When we encounter a new word, and append to the file, and when 
	 * we update an existing record, and rewrite a line
	 */
	private void writeToFile(String word, String definition) throws IOException {
		if(dict.containsKey(word)){ 
			ArrayList<String> currentDef = dict.get(word);
			currentDef.add(definition);
			dict.put(word, currentDef);
			
			String jsonString = generateWordJson(word, currentDef); // JSON with new K-V mapping

			File tempFile = new File("temp.txt");
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			
			String line; 
			while((line = reader.readLine()) != null) {
				String trimmed = line.trim();
				String recordWord = Json.read(trimmed).at("word").asString();
				if (recordWord.equals(word)) {
					writer.write(jsonString + System.getProperty("line.separator"));
				} else {
					writer.write(line + System.getProperty("line.separator"));
				}
			
			}
			writer.close();
			reader.close();
			filename.delete();
			tempFile.renameTo(filename);
		}
		else {
			ArrayList<String> newDef = new ArrayList<>();
			newDef.add(definition);
			dict.put(word, newDef);
			
			// append to the file with a new record
			String jsonString = generateWordJson(word, newDef);
			FileWriter fw = new FileWriter(filename, true);
			fw.write(jsonString + System.getProperty("line.separator"));
			fw.close();
		}
		
	}
	
	/* Remove a record by copying all records (except the one matching the word) to a new file,
	 * deleting the original file, and renaming the new file.
	 */
	private void removeFileRecord(String word) throws FileNotFoundException, IOException {
		File tempFile = new File("temp.txt");
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		String line; 
		
		while ((line = reader.readLine()) != null) {
			String trimmed = line.trim();
			String recordWord = Json.read(trimmed).at("word").asString();
			if (recordWord.equals(word)) {
				continue;
			}
			writer.write(line + System.getProperty("line.separator"));
		}
		writer.close();
		reader.close();
		filename.delete();
		tempFile.renameTo(filename);
	
	}
	
	private String generateWordJson(String word, ArrayList<String> definition) {
		String jsonString = Json.object()
				.set("word", word)
				.set("definition", definition).toString();
		return jsonString; 
	}
	
}
