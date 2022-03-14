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

public class downloadContentfromGit {
	
	static loginValues lvalues = loginValues.getInstance();
	static projectValues pvalues = projectValues.getInstance(); 
	
	public void downloadfromGit(String interval) {
		
		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
        dataObject.put("scan_code", pvalues.getScanCode());
		
        JSONObject rootObject = new JSONObject();
        rootObject.put("group", "scans");
        rootObject.put("action", "download_content_from_git");
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
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(httpClientResponse.getEntity().getContent(), "utf-8"));
			String result = br.readLine();
			
			System.out.println();			
			System.out.println("Start download source code from Git");
			System.out.println("Git URL: " + pvalues.getGitUrl() + "  /  Branch: " + pvalues.getGitBranch());			
			
			checkStatusDownloadingGit(interval);
			
			System.out.println();			
			System.out.println("End of downloading source code from Git");			
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void checkStatusDownloadingGit(String interval) {
		
		System.out.print("Downloading.");	
		
		JSONObject dataObject = new JSONObject();		
		dataObject.put("username", lvalues.getUsername());
	    dataObject.put("key", lvalues.getApikey());
	    dataObject.put("scan_code", pvalues.getScanCode());
	    
	    JSONObject rootObject = new JSONObject();
	    rootObject.put("group", "scans");
	    rootObject.put("action", "check_status_download_content_from_git");
		rootObject.put("data", dataObject);			
		
		String check = "t";
		
		try {
			while(check.equals("t")) {
				HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				
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
									
				
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpClientResponse.getEntity().getContent(), "utf-8"));
				String result = br.readLine();
				
				//System.out.println(result.toString());
				
				JSONParser jsonParser = new JSONParser();
			    JSONObject jsonObj1 = (JSONObject) jsonParser.parse(result.toString());
				
			    if(jsonObj1.get("status").toString().equals("1") && !jsonObj1.get("data").toString().equals("NOT FINISHED")) {
			    	check = "f";
			    }
			    
			    System.out.print(".");
			    
			    //1000 = 1 second
				int intervals = Integer.parseInt(interval) * 1000;
			    
			    Thread.sleep(intervals);			
			    
			    httpClient.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

}
