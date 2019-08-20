package data;

import mjson.Json;

public class Response {
	
	static String[] requestTypes = {"query", "add", "remove"}; 
	String requestType;
	String word;
	String body;
	
	public Response(String requestType, String word, String body) {
		this.requestType = requestType;
		this.word = word;
		this.body = body;
	}
	
	public Response(Json j) {
		String requestType = j.at("requestType").toString();
		String word = j.at("word").toString();
		String body = j.at("body").toString();
		
		if (requestType == null || word == null) {
			throw new IllegalArgumentException();
		} else {
			this.requestType = requestType;
			this.word = word;
			this.body = body;
		}
	}
	
	public String getJsonString() {
		Json j = Json.object().set("requestType", this.requestType)
				.set("word", this.word)
				.set("body", this.body);
		return j.toString();
	}
	


	public String getRequestType() {
		return this.requestType;
	}

	public String getWord() {
		return this.word;
	}

	public String getBody() {
		return this.body;
	}


	

}
