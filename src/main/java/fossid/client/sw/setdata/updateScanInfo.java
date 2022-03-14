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

public class updateScanInfo {
	
	loginValues lvalues = loginValues.getInstance();
	projectValues pvalues = projectValues.getInstance(); 
	
	public void updateScaninfo(String targetpath, String gitrepourl, String gitbranch, String gitrepourlchange, String gitbranchchange) {
		
		String end = "";
		String check = "f";
		
		if(!targetpath.equals("")) {
			end = targetpath.substring(targetpath.length() - 1, targetpath.length());
			if(!end.equals("/")) {
				targetpath = targetpath + "/";
			}
		}
		
		// to map scan to project
		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());        
        dataObject.put("project_code", pvalues.getProjectCode());
        dataObject.put("scan_code", pvalues.getScanCode());
        dataObject.put("scan_name", pvalues.getScanName());   	
       	if(!targetpath.equals("")) {
       		dataObject.put("target_path", targetpath);
       		
       		System.out.println("TARGETPATH: " + targetpath);
		} 
        
       	if(gitrepourlchange.equals("1")) {
       		dataObject.put("git_repo_url", gitrepourl);
		} 
       	
       	if(gitbranchchange.equals("1")) {       		
       		dataObject.put("git_branch", gitbranch);
		} 
       	
		JSONObject rootObject = new JSONObject();
        rootObject.put("group", "scans");
        rootObject.put("action", "update");
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
						
			JSONParser jsonParser = new JSONParser();
	        JSONObject jsonObj1 = (JSONObject) jsonParser.parse(result.toString());            
	        String getStatus = jsonObj1.get("status").toString();
			
	        if(getStatus.equals("1")) {
	        	System.out.println();
				System.out.println("The scanName/Code \"" + pvalues.getScanName() + " / " + pvalues.getScanCode() + "\" is assigned to " +
						"the projectName/code \"" + pvalues.getProjectName() + " / " + pvalues.getProjectCode() + "\""
				);	        	
	        } else {
	        	System.out.println();
				System.out.println("FAILED: The scanName/Code \"" + pvalues.getScanName() + " / " + pvalues.getScanCode() + "\" is assinged to " +
						"the projectName/code \"" + pvalues.getProjectName() + " / " + pvalues.getProjectCode() + "\""
				);
				System.out.println("Please, check the FossID configuration file, /fossid/etc/fossid.conf and set 'webapp_scan_path_enable' to '1'.");
	        }	        
			
		} catch (Exception e) {
			e.printStackTrace();
			pvalues.setSuccess(0);
			System.exit(1);
		}			
	}

}
