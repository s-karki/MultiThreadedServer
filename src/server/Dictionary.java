package server;

import java.util.HashMap;
import java.util.concurrent.locks.*;

import mjson.Json;

import java.io.*;

public class Dictionary {
	File filename;
	int size; 
	private final ReadWriteLock lock; 
	HashMap<String, String> dict;
	
	
	/* results are written to a textfile, and loaded into memory 
	 * on startup
	 */
	public Dictionary(File filename) {
		this.filename = filename;
		this.lock = new ReentrantReadWriteLock();
		size = 0; 
		
	}
	
	public void loadIntoMemory() throws FileNotFoundException, IOException {
		HashMap<String, String> readIn = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String record;
		while((record = reader.readLine()) != null) {
			// parse JSON
			Json j = Json.read(record);
			String word = j.at("word").toString();
			String definition = j.at(record).toString();
			readIn.put(word, definition);
		}
		dict = readIn; 
		reader.close();
	}
	
	public String query(String word) {
		String res;
		lock.readLock().lock();
		try {
			res = dict.get(word);
		} finally {
			lock.readLock().unlock();
		}
		String ret = (res == null) ? "" : res; 
		return ret; 
	}
	
	/* query the word – if we find match, we overwrite it
	 * if no match, we add a new record
	 */ 
	public String add(String word, String definition) {		
		if (word == null || word.equals("") || definition == null || definition.equals("")) {
			return "Error: the word or definition you supplied was empty. The dictionary will"
					+ " not be updated"; 
		}
		
		boolean success = false; 
		String append = "";
		lock.writeLock().lock();
		try {
			if(dict.get(word) != null){ 
				dict.remove(word);
				removeFileRecord(word);
			}		
			dict.put(word, definition);
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			String jsonString = Json.object().set("word", word).set("definition", definition)
					.toString();
			writer.write(jsonString + System.getProperty("line.separator"));
			writer.close();
			size++;
			success = true;
		}
		catch (IOException e) {
			success = false;
			append = ": There was an IO Error writing to the dictionary file";
		} finally {
			lock.writeLock().unlock();
		}
		
		return Boolean.toString(success) + append; 
		
	}
	
	public String remove(String word) {
		if (word == null || word.equals("")) {
			return "Error: the word you supplied was empty. "
					+ "The dictionary will not be updated";
		}
		boolean success = false; 
		String append = ""; 
		
		lock.writeLock().lock();
		try {
			dict.remove(word);
			removeFileRecord(word);
			size--; 
			success = true;
			append = ""; 
		} 
		catch (IOException e) {
			success = false; 
			append = ": There was an IO Error writing to the dictionary file";
		}
		finally {
			lock.writeLock().unlock();
		}
		return Boolean.toString(success) + append; 
	}
	
	
	private void removeFileRecord(String word) throws FileNotFoundException, IOException {
		File tempFile = new File("temp.txt");
		
		
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		String line; 
		
		while ((line = reader.readLine()) != null) {
			String trimmed = line.trim();
			String recordWord = Json.read(trimmed).at("word").toString();
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
	

	
}
