package client;

public class ClientException {
	
	public static void printMessageAndExit(String msg, Exception e) {
		System.out.println(msg);
		e.printStackTrace();
		System.exit(1);
	}
}
