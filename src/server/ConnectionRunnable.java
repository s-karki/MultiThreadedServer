package server;

import java.net.*;
import java.io.*;
import mjson.Json; 
import data.*;
import exception.ExceptionHandler;

public class ConnectionRunnable implements Runnable {
	
	Socket conn; 
	
	public ConnectionRunnable(Socket conn) {
		this.conn = conn; 
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
			// TODO Auto-generated catch block
			ExceptionHandler.printMessage("Could not read from the client", e);
			return; 
		}
		Request req = new Request(Json.read(in));
		String requestType = req.getRequestType();
		String word = req.getWord();
		
		//handle read
		if (requestType.equals("query"));
		
		
		
		
	}

}
