package data;

import java.util.ArrayList;
import java.util.List;

import mjson.Json;

public class Response {
	
	static String[] requestTypes = {"query", "add", "remove"}; 
	String requestType;
	String word;
	ArrayList<String> body;
	
	public Response(String requestType, String word, ArrayList<String> body) {
		this.requestType = requestType;
		this.word = word;
		this.body = body;
	}
	
	public Response(Json j) {
		String requestType = j.at("requestType").asString();
		String word = j.at("word").asString();
		List<Object> body = j.at("body").asList();
		ArrayList<String> responseList = new ArrayList<>();
		
		if (requestType == null || word == null) {
			throw new IllegalArgumentException();
		} else {
			this.requestType = requestType;
			this.word = word;
			for (Object o : body) {
				responseList.add(o.toString());
			}
			this.body = responseList; 
			
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

	public ArrayList<String> getBody() {
		return this.body;
	}


	

}
