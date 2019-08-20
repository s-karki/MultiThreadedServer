package data;

import mjson.Json;

public class Request {
	
	static String[] requestTypes = {"query", "add", "remove"}; 
	String requestType; 
	String word; 
	String definition; 
	
	public Request(String requestType, String word) {
		this.requestType = requestType;
		this.word = word;
		this.definition = null;
	}
	public Request(String requestType, String word, String definition) {
		this.requestType = requestType;
		this.word = word;
		this.definition = definition; 
	}
	
	
	// translate from Json to request
	public Request(Json j) {
		String r = j.at("requestType").toString();
		String w = j.at("word").toString();
		String d = j.at("definition").toString();
		
		
		if (requestType == null || word == null) {
			throw new IllegalArgumentException();
		} else {
			requestType = r;
			word = w; 
			definition = d; 
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
		return word;
	}
	
	public String getDefinition() {
		return definition; 
	}
	
	public String getRequestType() {
		return requestType; 
	}

}
