package client;

import java.net.*;
import java.util.Scanner;

import data.*;

import java.io.*;
import mjson.Json; 


public class Client {

	public static void main(String[] args) throws IOException {
		String host = args[0];
		int port = Integer.parseInt(args[1]); 
		Scanner s = new Scanner(System.in);
		
		Socket conn = null;
		try {
			 conn = new Socket(host, port);
		} catch (UnknownHostException e) {
			ClientException.printMessageAndExit("Error: We could not find the host", e);
		} catch (IOException e) {
			ClientException.printMessageAndExit("Error: An IO Error occured when initialising "
					+ "socket", e); 
		}
		
		DataInputStream din = new DataInputStream(conn.getInputStream());
		DataOutputStream dout = new DataOutputStream(conn.getOutputStream());
		
		while(true) {
			String[] cmd = s.nextLine().split(" ");
			if (cmd[0].equals("exit")) {
				System.out.println("Exiting");
				din.close();
				dout.close();
				conn.close();
			}
			
			String requestType = cmd[1];
			String word = cmd[2]; 
			//Get rid of this for GUI
			if(Request.isValidRequestType(requestType) == false) {
				System.out.println("Invalid request type");
				continue; 
			}
			//write the request to JSON and send
			String out = new Request(requestType, word).getJsonString();
			dout.writeUTF(out);
			
			//get response and display
			String in = din.readUTF();
			Response res = new Response(Json.read(in)); // need error handling (illegal argument)??
			
			System.out.println(res.getRequestType());
			System.out.println(res.getBody());
			
			
		
		}
		
	}

}
