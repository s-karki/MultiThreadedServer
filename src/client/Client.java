package client;

import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

import data.*;
import exception.ExceptionHandler;

import java.io.*;
import mjson.Json; 


public class Client {

	public static void main(String[] args) throws IOException {
		String host = args[0]; //handle when there is no host
		int port = Integer.parseInt(args[1]); 
		Scanner s = new Scanner(System.in);
		
		Socket conn = null;
		try {
			 conn = new Socket(host, port);
		} catch (UnknownHostException e) {
			ExceptionHandler.printMessageAndExit("Error: We could not find the host", e);
		} catch (IOException e) {
			ExceptionHandler.printMessageAndExit("Error: An IO Error occured when initialising "
					+ "socket", e); 
		}
		
		DataInputStream din = new DataInputStream(conn.getInputStream());
		DataOutputStream dout = new DataOutputStream(conn.getOutputStream());
		
		while(true) {
			String input = s.nextLine();
			String[] cmd = input.split(" ");
			if (cmd[0].equals("exit")) {
				System.out.println("Exiting");
				din.close();
				dout.close();
				conn.close();
				s.close();
				System.exit(0);
			}
			
			
			String requestType = cmd[0];
			String word = cmd[1]; 
			//Get rid of this for GUI
			if(Request.isValidRequestType(requestType) == false) {
				System.out.println("Invalid request type");
				continue; 
			}
			//write the request to JSON and send
			String out; 
			if (requestType.equals("add")) {
				if (cmd.length < 3) {
					System.out.println("Invalid request type: no definition");
					continue; 
				}
				//get the definition as a string
				String[] arrayDefinition = Arrays.copyOfRange(cmd, 2, cmd.length); 
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < arrayDefinition.length; i++) {
					if (i < arrayDefinition.length - 1) {
						sb.append(arrayDefinition[i]);
						sb.append(" ");
					} else {
						sb.append(arrayDefinition[i]);
					}
				}
				String definition = sb.toString();
				out = new Request(requestType, word, definition).getJsonString();
				
				//System.out.println(definition);
				//System.out.println(out);
				
			} else {
				out = new Request(requestType, word).getJsonString();
			}
			dout.writeUTF(out);

			//get response and display
			String in = din.readUTF();
			Response res = new Response(Json.read(in)); // need error handling (illegal argument/server dies)??
			
			System.out.println("\n");
			System.out.println(res.getRequestType());
			System.out.println(res.getWord());
			System.out.println(res.getBody());
			
			
		
		}
		
	}

}
