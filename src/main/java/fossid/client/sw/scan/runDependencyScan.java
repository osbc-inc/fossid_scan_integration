package fossid.client.sw.scan;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fossid.client.sw.values.loginValues;
import fossid.client.sw.values.projectValues;

public class runDependencyScan {
	
	loginValues lvalues = loginValues.getInstance();
	projectValues pvalues = projectValues.getInstance(); 
	
	public void runDependencyScan(String interval) {
		
		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
        dataObject.put("scan_code", pvalues.getScanCode());
		
        JSONObject rootObject = new JSONObject();
        rootObject.put("group", "scans");
        rootObject.put("action", "run_dependency_analysis");
		rootObject.put("data", dataObject);
		
		HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
		HttpClient httpClient = HttpClientBuilder.create().build();						
		
		try {

			StringEntity entity = new StringEntity(rootObject.toString());
			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(entity);
					
			HttpResponse httpClientResponse = httpClient.execute(httpPost);		
			
			if (httpClientResponse.getStatusLine().getStatusCode() != 200) {								
				pvalues.setSuccess(0);
				System.out.println();
				System.out.println("FAILED: HTTP Error code: " + httpClientResponse.getStatusLine().getStatusCode());
				System.out.println();
				System.exit(1);	
			}
			
			System.out.println();
			System.out.println("Dependency Scan has been launched!!");
			System.out.println();			
					
			checkScanStatus(interval);
			
			System.out.println();	
			System.out.println("DependencyScan has been finished!!");
			System.out.println();			
			
		} catch (Exception e) {				
			e.printStackTrace();
			pvalues.setSuccess(0);
			System.exit(1);
		}		
	}
	

	private void checkScanStatus(String interval) {

		// to map scan to project
		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
        dataObject.put("scan_code", pvalues.getScanCode());
        dataObject.put("type", "DEPENDENCY_ANALYSIS");
        
		JSONObject rootObject = new JSONObject();
        rootObject.put("group", "scans");
        rootObject.put("action", "check_status");
		rootObject.put("data", dataObject);		
						
		String finished = "false";
		//1000 = 1 second
		int intervals = Integer.parseInt(interval) * 1000;
		
		try {
			int i = 1;
			//int loopCount = 1;
			
			while(finished.equals("false")) {
				HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();		
					
				HttpResponse httpClientResponse = null;
			
				//int postCount = 1;			
					
				StringEntity entity = new StringEntity(rootObject.toString());
				httpPost.addHeader("content-type", "application/json");
				httpPost.setEntity(entity);
				httpClientResponse = httpClient.execute(httpPost);					
			
				if (httpClientResponse.getStatusLine().getStatusCode() != 200) {								
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
			    JSONObject jsonObj1 = (JSONObject) jsonParser.parse(result.toString());            
			    JSONObject jsonObj2 = (JSONObject) jsonObj1.get("data");
			       		        
			    if(jsonObj2.get("finished") != null && !jsonObj2.get("is_finished").toString().equals("false")) {
			        finished = jsonObj2.get("finished").toString();		        	
			    }		       
			       
			    /**
			    if(jsonObj2.get("comment") != null) {
			    	System.out.println(jsonObj2.get("comment"));
			    }
			    **/
			    
			    if(i==1) {
			    	System.out.print("Running Dependency Analysis");
			    	i++;
			    }
			    System.out.print(".");
			        
			    Thread.sleep(intervals);	
			    
			    httpClient.close();
		    }
			
			System.out.println();
			
		} catch (Exception e) {			
			e.printStackTrace();
			pvalues.setSuccess(0);
			System.exit(1);
		}
	}

}
