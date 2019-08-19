package data;

import mjson.Json;

public class Request {
	
	static String[] requestTypes = {"query", "add", "remove"}; 
	String requestType; 
	String word; 
	
	public Request(String requestType, String word) {
		this.requestType = requestType;
		this.word = word;
	}
	
	public Request(Json j) {
		String requestType = j.at("requestType").toString();
		String word = j.at("word").toString();
		if (requestType == null || word == null) {
			throw new IllegalArgumentException();
		} else {
			this.requestType = requestType;
			this.word = word; 
		}
	}
	
	public static boolean isValidRequestType(String type) {
		for(int i = 0; i < requestTypes.length; i++) {
			if (requestTypes[i].equals(type)) {
				return true;
			}
		}
		return false; 
	}
	
	public String getJsonString() {
		Json j = Json.object().set("requestType", this.requestType)
				.set("word", this.word); 
		return j.toString();
	}
	
	
	public String getWord() {
		return this.word;
	}
	
	public String getRequestType() {
		return this.requestType; 
	}

}
