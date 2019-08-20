package server;

import java.net.*;
import java.io.*;
import mjson.Json; 
import data.*;
import exception.ExceptionHandler;

public class ConnectionRunnable implements Runnable {
	
	Socket conn; 
	Dictionary dict;
	
	public ConnectionRunnable(Socket conn, Dictionary dict) {
		this.conn = conn; 
		this.dict = dict; 
	}
	
	public void run() {
		DataInputStream din = null;
		DataOutputStream dout = null;
		try {
			 din = new DataInputStream(conn.getInputStream());
			 dout = new DataOutputStream(conn.getOutputStream());
		} catch (IOException e) {
			ExceptionHandler.printMessage("Could not initialise input and output streams", e);
			return;
		}
		
		String in; 
		try {
			in = din.readUTF();
		} catch (IOException e) {
			ExceptionHandler.printMessage("Could not read from the client", e);
			return; 
		}
		Request req = new Request(Json.read(in));
		String requestType = req.getRequestType();
		String word = req.getWord();
		String definition = req.getDefinition();
		
		String response = ""; 
		switch (requestType) {
			case "add":
				response = dict.add(word, definition);
			case "remove":
				response = dict.remove(word);
			case "query":
				response = dict.query(word);
		}
		
		//write the requestType, word, definition (if any) / body to the client
		
				

	}

}
