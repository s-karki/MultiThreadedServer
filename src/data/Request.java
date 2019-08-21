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
		String r = j.at("requestType").asString();
		String w = j.at("word").asString();
		String d = null;
		if (r.equals("add")) {
			d = j.at("definition").asString();
		}
		
		if (r == null || w == null) {
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
		Json j = Json.object().set("requestType", requestType)
				.set("word", word)
				.set("definition", definition); 
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

	public String toString() {
		return
				"type: " + requestType + "\n"
				+ "word: " + word + "\n"
				+ "definition: " + definition;
	}
	
}
