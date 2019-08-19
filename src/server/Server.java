package server;

import java.net.*;
import java.io.*;
import data.*; 
import mjson.Json;

class Server {

	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt(args[0]);
		ServerSocket server = new ServerSocket(port);
		
		while(true) {
			Socket connection = server.accept();
			
		}
		
	}
	
}
