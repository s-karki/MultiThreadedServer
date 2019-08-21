package server;

import java.net.*;
import java.io.*;
import data.*; 
import mjson.Json;

/* USAGE: java Server {PORT}
 * 
 */

class Server {

	public static void main(String[] args) throws IOException {
		// test if the dictionary file exists
		File f = new File("dictionary.txt");
		//create a dictionary file if it does not exist, and load records into memory
		f.createNewFile(); 
		Dictionary dict = new Dictionary(f);
		dict.loadIntoMemory(); // error handle here 
		
		int port = Integer.parseInt(args[0]);
		ServerSocket server = new ServerSocket(port);		
	
		while(true) {
			Socket conn = server.accept();
			System.out.println("New Socket opened to " + conn.getInetAddress() + " on port "
			+ port);
			new Thread(new ConnectionRunnable(conn, dict)).start();
			
		}
		
	}
	
}
