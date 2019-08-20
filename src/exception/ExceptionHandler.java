package exception;

public class ExceptionHandler {
	
	public static void printMessageAndExit(String msg, Exception e) {
		System.out.println(msg);
		e.printStackTrace();
		System.exit(1);
	}
	
	public static void printMessage(String msg, Exception e) {
		System.out.println(msg);
		e.printStackTrace();
	}
}
