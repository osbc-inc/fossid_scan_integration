package fossid.client.sw.setdata;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fossid.client.sw.values.loginValues;
import fossid.client.sw.values.projectValues;

public class setLoginInfo {
	
	loginValues lvalues = loginValues.getInstance();
	projectValues pvalues = projectValues.getInstance();
	
	public void setLogininfo(String protocol, String address, String userName, String apiKey) {
		
		// Set the uri with http or https according to fossid.dprotocol
		if(protocol.equals("http")) {
			lvalues.setServerUri("http://" + address);
			
			//check "fossid.domain" to add / in front of api.php
			String temp = lvalues.getServerUri();
			temp = temp.substring(temp .length() - 1, temp.length());
			
			if(temp.equals("/")) {
				lvalues.setServerApiUri("http://" + address + "api.php");
				lvalues.setServerUploadUri("http://" + address + "api.php");
			} else {
				lvalues.setServerApiUri("http://" + address + "/api.php");
				lvalues.setServerUploadUri("http://" + address + "/api.php");
			}				
		} else if(protocol.equals("https")) {
			lvalues.setServerUri("https://" + address);
			
			//check "fossid.domain" to add / in front of api.php
			String temp = lvalues.getServerUri();				
			temp = temp.substring(temp .length() - 1, temp.length());
			
			if(temp.equals("/")) {
				lvalues.setServerApiUri("https://" + address + "api.php");
				lvalues.setServerUploadUri("https://" + address + "api.php");
			} else {
				lvalues.setServerApiUri("https://" + address + "/api.php");
				lvalues.setServerUploadUri("https://" + address + "/api.php");
			}
		}
		
		lvalues.setUsername(userName);			
		lvalues.setApikey(apiKey);
		
		validateAuthentication();
	}
	
	private void validateAuthentication() {
		
		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
        dataObject.put("searched_username", lvalues.getUsername());
		
		JSONObject rootObject = new JSONObject();
        rootObject.put("group", "users");
        rootObject.put("action", "get_information");
		rootObject.put("data", dataObject);		
		
		HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
		HttpClient httpClient = HttpClientBuilder.create().build();		
		
		try {

			StringEntity entity = new StringEntity(rootObject.toString());
			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(entity);
			
			HttpResponse httpClientResponse = httpClient.execute(httpPost);		
			
			if (httpClientResponse.getStatusLine().getStatusCode() != 200) {
				System.out.println("FAILED: Please, check the FOSSID protocol or url values");				
				pvalues.setSuccess(0);
				System.out.println();
				System.out.println("FAILED: HTTP Error code: " + httpClientResponse.getStatusLine().getStatusCode());
				System.out.println();
				System.exit(1);		
			}
		
			BufferedReader br = new BufferedReader(
					new InputStreamReader(httpClientResponse.getEntity().getContent(), "utf-8"));
			String result = br.readLine();
			
			JSONParser jsonParser = new JSONParser();
	        JSONObject jsonObj = (JSONObject) jsonParser.parse(result.toString());
	        
	        // set false if valiation is failed 
	        if(jsonObj.get("status").equals("0")){
	        	System.out.println("FAILED: Please, check the FOSSID username or apikey values");
	        	pvalues.setSuccess(0);
				System.exit(1);
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
			pvalues.setSuccess(0);
			System.exit(1);
		}	
	}

}
