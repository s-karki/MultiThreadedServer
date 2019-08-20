package server;

import java.util.HashMap;
import java.util.concurrent.locks.*;

public class Dictionary {
	String filename;
	int size; 
	private final ReadWriteLock lock; 
	HashMap<String, String> dict;
	
	
	// execute constructor on startup
	/* results are written to a textfile, and loaded into memory 
	 * on startup
	 */
	public Dictionary(String filename) {
		this.filename = filename;
		this.lock = new ReentrantReadWriteLock();
		
	}
	
	public loadIntoMemory() {
		
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
	
	public String add(String word) {
		// add to text file, then to dict
	}
	
	public String remove(String word) {
		// remove from dict, then from text file
	}
	
	
	
}
