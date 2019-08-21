package server;

import java.net.*;
import java.util.ArrayList;
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
		//TODO: timeout after disconnect
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
		while(true) {
			try {
				in = din.readUTF();
			} catch (IOException e) {
				System.out.println("Client has disconnected");
				try {
				din.close(); //attempt to gracefully disconnect. 
				dout.close();
				conn.close();
				} catch(IOException e1) {
					break; //kill the thread
				}
				break; 
			}
			
			Request req = new Request(Json.read(in));	
			String requestType = req.getRequestType();
			String word = req.getWord();
			String definition = req.getDefinition();
			
			ArrayList<String> response = new ArrayList<>(); 	
					
			switch (requestType) {
				case "add":
					response.add(dict.add(word, definition));
					break;
				case "remove":
					response.add(dict.remove(word));
					break;
				case "query":
					response.addAll(dict.query(word));
					break;
			}
			String msg = new Response(requestType, word, response).getJsonString();
			try {
				dout.writeUTF(msg);
			} catch (IOException e){
				ExceptionHandler.printMessage("Error: Could not write to the client", e);
				return; 
			}
		}
		System.out.println("thread complete, waiting for a new one");

	}

}
